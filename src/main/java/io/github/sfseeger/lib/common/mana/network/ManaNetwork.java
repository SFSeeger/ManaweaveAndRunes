package io.github.sfseeger.lib.common.mana.network;

import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ManaNetwork {
    private final UUID id;

    public static final Comparator<ManaNetworkNode> NODE_COMPARATOR =
            Comparator.comparingInt(ManaNetworkNode::getPriority)
                    .reversed()
                    .thenComparingLong(value -> value.getBlockPos().asLong());
    public static final Comparator<ManaTransaction> TRANSACTION_COMPARATOR =
            Comparator.comparingInt(i -> i.node().getPriority());

    Set<ManaNetworkNode> nodes = new LinkedHashSet<>();
    Set<ManaNetworkNode> receiverNodes = new LinkedHashSet<>();
    Set<ManaNetworkNode> providerNodes = new LinkedHashSet<>();
    Set<ManaNetworkNode> hybridNodes = new TreeSet<>(NODE_COMPARATOR);

    Queue<ManaTransaction> manaRequests = new PriorityQueue<>(TRANSACTION_COMPARATOR.reversed());
    Queue<ManaTransaction> manaOffers = new PriorityQueue<>(TRANSACTION_COMPARATOR.reversed());

    public ManaNetwork(@Nullable UUID id) {
        this.id = id != null ? id : UUID.randomUUID();
    }

    public void requestMana(ManaTransaction transaction) {
        manaRequests.add(transaction);
    }

    public void provideMana(ManaTransaction transaction) {
        manaOffers.add(transaction);
    }

    public void distributeMana() {
        Map<Mana, Integer> totalRequested = new HashMap<>();
        Map<Mana, Integer> totalOffered = new HashMap<>();

        for (ManaTransaction request : manaRequests) {
            totalRequested.merge(request.mana(), request.amount(), Integer::sum);
        }

        for (ManaTransaction offer : manaOffers) {
            totalOffered.merge(offer.mana(), offer.amount(), Integer::sum);
        }

        Set<Mana> allMana = new HashSet<>(totalRequested.keySet());
        allMana.addAll(totalOffered.keySet());

        for (Mana mana : allMana) {
            int request = totalRequested.getOrDefault(mana, 0);
            int offer = totalOffered.getOrDefault(mana, 0);
            int diff = offer - request;

            if (diff > 0) {
                for (ManaNetworkNode hybrid : hybridNodes) {
                    int amount = hybrid.receiveMana(diff, mana);
                    offer -= amount;
                    diff -= amount;
                    if (diff == 0) break;
                }
            } else {
                for (ManaNetworkNode hybrid : hybridNodes) {
                    if (hybrid.hasMana(mana)) {
                        int amount = hybrid.extractMana(-diff, mana);
                        offer += amount;
                        diff += amount;
                        if (diff == 0) break;
                    }
                }
            }
            totalOffered.put(mana, offer);

            if (offer > 0) {
                manaRequests.stream().filter(i -> i.mana().equals(mana)).forEach(i -> {
                    int amountToReceive = Math.min(i.amount(), totalOffered.getOrDefault(mana, 0));
                    i.node().receiveMana(amountToReceive, mana);
                    totalOffered.put(mana, totalOffered.getOrDefault(mana, 0) - amountToReceive);
                });
            }

            boolean overSaturated = diff > 0;
            int extracted = manaOffers.stream()
                    .filter(i -> i.mana().equals(mana))
                    .filter(i -> !overSaturated || i.node().shouldExtractWhenSaturated())
                    .mapToInt(of -> of.node().extractMana(of.amount(), of.mana()))
                    .sum();

            if (extracted < request && overSaturated) {
                for (ManaTransaction of : manaOffers.stream()
                        .filter(i -> i.mana().equals(mana) && i.node().shouldExtractWhenSaturated())
                        .toList()) {
                    int amount = of.node().extractMana(request - extracted, mana);
                    extracted += amount;
                    if (extracted >= request) {
                        break;
                    }
                }
            }

            if(diff != 0) {
                System.out.println("ManaNetwork.distributeMana: diff != 0 for mana " + mana + " diff: " + diff);
            }
        }

        manaRequests.clear();
        manaOffers.clear();
    }

    public void addNode(ManaNetworkNode node) {
        nodes.add(node);
        switch (node.getNodeType()) {
            case PROVIDER -> providerNodes.add(node);
            case RECEIVER -> receiverNodes.add(node);
            case HYBRID -> hybridNodes.add(node);
        }
    }

    public void removeNode(ManaNetworkNode node) {
        nodes.remove(node);
        receiverNodes.remove(node);
        providerNodes.remove(node);
        hybridNodes.remove(node);
    }

    public BFSResult checkValidity(ManaNetworkNode start) {
        Queue<ManaNetworkNode> queue = new LinkedList<>();
        Set<ManaNetworkNode> visited = new HashSet<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            ManaNetworkNode current = queue.poll();
            visited.add(current);
            for (ManaNetworkNode connected : current.getConnectedNodes()) {
                if (!visited.contains(connected)) {
                    queue.add(connected);
                }
            }
        }
        return new BFSResult(visited.size() == nodes.size(), visited);
    }

    public void splitNetwork(ManaNetworkNode start, ServerLevel level) {
        start.disconnectAllNodes();
        removeNode(start);

        if (!nodes.isEmpty() && nodes.size() > 1) {
            ManaNetworkNode anyNode = nodes.iterator().next(); // Get any remaining node
            BFSResult result = checkValidity(anyNode);

            if (result.isValid()) return;
        }

        ManaNetworkHandler handler = ManaNetworkHandler.getInstance(level.getDataStorage());
        Set<ManaNetworkNode> visited = new HashSet<>();

        for (ManaNetworkNode node : nodes) {
            if (!visited.contains(node)) {
                BFSResult result = checkValidity(node);
                if (result.visitedNodes().size() == 1 && node.getConnectedNodes().isEmpty()) {
                    node.setManaNetworkNoEval(null);
                } else {
                    ManaNetwork newNetwork = handler.createNetwork();
                    for (ManaNetworkNode connectedNode : result.visitedNodes()) {
                        newNetwork.addNode(connectedNode);
                        connectedNode.setManaNetworkNoEval(newNetwork);
                        visited.add(connectedNode);
                        connectedNode.setChanged();
                    }
                }
            }
        }
        handler.removeNetwork(this);
    }

    public void merge(ManaNetwork network, ServerLevel level) {
        ManaNetwork target;
        ManaNetwork source;
        if (nodes.size() > network.nodes.size()) {
            target = this;
            source = network;
        } else {
            target = network;
            source = this;
        }

        target.nodes.addAll(source.nodes);
        target.receiverNodes.addAll(source.receiverNodes);
        target.providerNodes.addAll(source.providerNodes);
        target.hybridNodes.addAll(source.hybridNodes);
        target.manaRequests.addAll(source.manaRequests);
        target.manaOffers.addAll(source.manaOffers);

        for (ManaNetworkNode node : source.nodes) {
            node.setManaNetworkNoEval(this);
        }
        ManaNetworkHandler.getInstance(level.getDataStorage()).removeNetwork(source);
    }


    public UUID getId() {
        return id;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public void tick() {
        distributeMana();
    }

    public record BFSResult(boolean isValid, Set<ManaNetworkNode> visitedNodes) {
    }
}
