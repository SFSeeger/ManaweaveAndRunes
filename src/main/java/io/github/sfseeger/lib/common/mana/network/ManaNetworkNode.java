package io.github.sfseeger.lib.common.mana.network;

import io.github.sfseeger.lib.common.LibUtils;
import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ManaNetworkNode {
    private final Set<ManaNetworkNode> connectedNodes = new LinkedHashSet<>();
    private final BlockEntity blockEntity;
    private ManaNetworkNodeType nodeType;
    public List<BlockPos> pendingConnections = new ArrayList<>();
    private UUID networkId;
    private int priority = 0;
    private ManaNetwork manaNetwork;
    private boolean shouldExtractWhenSaturated = true;

    public ManaNetworkNode(BlockEntity blockEntity, ManaNetworkNodeType type, int priority,
            boolean shouldExtractWhenSaturated) {
        this(blockEntity, type);
        this.priority = priority;
        this.shouldExtractWhenSaturated = shouldExtractWhenSaturated;
    }

    public ManaNetworkNode(BlockEntity blockEntity, ManaNetworkNodeType type) {
        if (!(blockEntity instanceof IManaNetworkSubscriber manaBE)) {
            throw new IllegalArgumentException("BlockEntity must implement IManaNetworkSubscriber");
        }
        this.blockEntity = blockEntity;
        this.nodeType = type;
    }

    public static Optional<ManaNetworkNode> deserializeNBT(CompoundTag tag, HolderLookup.Provider registries,
                                                           BlockEntity blockEntity) {
        if (!tag.contains("mana_network") || !tag.contains("type")) return Optional.empty();

        boolean performLevelOperations = blockEntity.getLevel() != null;
        boolean performServerOperations = performLevelOperations && !blockEntity.getLevel().isClientSide();

        ManaNetworkNodeType type =
                tag.get("type") != null ? ManaNetworkNodeType.values()[tag.getInt("type")] : ManaNetworkNodeType.HYBRID;
        UUID manaNetworkId = tag.getUUID("mana_network");
        int priority = tag.getInt("priority");
        boolean shouldExtractWhenSaturated = tag.getBoolean("should_extract_when_saturated");

        ManaNetworkNode node = new ManaNetworkNode(blockEntity, type);
        node.setNetworkId(manaNetworkId);
        node.setPriority(priority);
        node.shouldExtractWhenSaturated = shouldExtractWhenSaturated;

        node.pendingConnections.addAll(
                BlockPos.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("connected_nodes")).result().orElse(List.of()));

        if (performLevelOperations && blockEntity.getLevel()
                .getBlockEntity(blockEntity.getBlockPos()) instanceof IManaNetworkSubscriber) {
            node.connectPendingNodes();
        }
        if (performServerOperations) {
            ServerLevel level = (ServerLevel) blockEntity.getLevel();
            getNetworkFromUUID(manaNetworkId, level).ifPresent(node::setManaNetwork);
        }

        return Optional.of(node);
    }

    public static Optional<ManaNetwork> getNetworkFromUUID(UUID id, ServerLevel level) {
        return Optional.ofNullable(ManaNetworkHandler.getInstance(level.getDataStorage()).manaNetworks.get(id));
    }

    public void connectPendingNodes() {
        if (blockEntity.getLevel() == null || pendingConnections.isEmpty())
            return;

        pendingConnections.forEach(pos -> {
            BlockEntity be = blockEntity.getLevel().getBlockEntity(pos);
            if (be instanceof IManaNetworkSubscriber) {
                ManaNetworkNode node = ((IManaNetworkSubscriber) be).getManaNetworkNode();
                if (node != null) {
                    connectedNodes.add(node);
                }
            }
        });
        pendingConnections.clear();
    }

    public Optional<ManaNetwork> getManaNetwork() {
        if (blockEntity.getLevel() == null || blockEntity.getLevel().isClientSide || manaNetwork == null) {
            return Optional.empty();
        }
        return Optional.of(manaNetwork);
    }

    public void setManaNetwork(@Nullable ManaNetwork manaNetwork) {
        if (this.blockEntity.getLevel() != null && !this.blockEntity.getLevel().isClientSide()) {
            if (this.manaNetwork != null) this.manaNetwork.removeNode(this);
            setManaNetworkNoEval(manaNetwork);
            if (manaNetwork != null) manaNetwork.addNode(this);
            ((IManaNetworkSubscriber) this.blockEntity).markUpdated();
        }
    }

    public void setManaNetworkNoEval(@Nullable ManaNetwork manaNetwork) {
        this.manaNetwork = manaNetwork;
        UUID id = manaNetwork != null ? manaNetwork.getId() : null;
        setNetworkId(id);
    }

    public void updateNetwork() {
        if (blockEntity.getLevel() == null || blockEntity.getLevel().isClientSide()) return;
        getNetworkFromUUID(networkId, (ServerLevel) blockEntity.getLevel()).ifPresent(this::setManaNetwork);
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public void connectNode(ManaNetworkNode node) {
        connectedNodes.add(node);
        node.connectedNodes.add(this);

        if (blockEntity.getLevel() == null || blockEntity.getLevel().isClientSide) return;

        boolean hasNetwork = getManaNetwork().isPresent();
        boolean nodeHasNetwork = node.getManaNetwork().isPresent();
        ServerLevel level = (ServerLevel) blockEntity.getLevel();

        if (hasNetwork && nodeHasNetwork) {
            ManaNetwork network = getManaNetwork().get();
            ManaNetwork nodeNetwork = node.getManaNetwork().get();
            if (network != nodeNetwork) {
                network.merge(nodeNetwork, level);
            }
        } else if (hasNetwork) {
            node.setManaNetwork(getManaNetwork().get());
        } else if (nodeHasNetwork) {
            setManaNetwork(node.getManaNetwork().get());
        } else {
            ManaNetwork newNetwork = ManaNetworkHandler.getInstance(level.getDataStorage()).createNetwork();
            setManaNetwork(newNetwork);
            node.setManaNetwork(newNetwork);
        }
        ((IManaNetworkSubscriber) this.blockEntity).markUpdated();
        ((IManaNetworkSubscriber) node.blockEntity).markUpdated();
    }

    /*
     * Disconnects a node without evaluating the network
     * This is used when splitting networks
     */
    public void disconnectNodeWithoutEval(ManaNetworkNode node) {
        connectedNodes.remove(node);
        node.connectedNodes.remove(this);
        //TODO: Remove network if no connected nodes
        ((IManaNetworkSubscriber) this.blockEntity).markUpdated();
        ((IManaNetworkSubscriber) node.blockEntity).markUpdated();
    }

    public void disconnectAllNodes() {
        connectedNodes.forEach(node -> {
            node.connectedNodes.remove(this);
            ((IManaNetworkSubscriber) node.blockEntity).markUpdated();
        });
        connectedNodes.clear();
        ((IManaNetworkSubscriber) this.blockEntity).markUpdated();
    }

    public void disconnectNode(ManaNetworkNode node) {
        disconnectNodeWithoutEval(node);

        if (blockEntity.getLevel() == null || blockEntity.getLevel().isClientSide) return;

        boolean hasNetwork = getManaNetwork().isPresent();
        boolean nodeHasNetwork = node.getManaNetwork().isPresent();
        ServerLevel level = (ServerLevel) blockEntity.getLevel();

        if (hasNetwork && nodeHasNetwork) {
            ManaNetwork network = getManaNetwork().get();
            network.splitNetwork(this, level);
        }
        ((IManaNetworkSubscriber) this.blockEntity).markUpdated();
        ((IManaNetworkSubscriber) node.blockEntity).markUpdated();
    }

    public Set<ManaNetworkNode> getConnectedNodes() {
        return connectedNodes;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int receiveMana(int amount, Mana mana) {
        IManaHandler handler = getManaHandler();
        if (handler == null) return 0;
        return handler.receiveMana(amount, mana, false);
    }

    public int extractMana(int amount, Mana mana) {
        IManaHandler handler = getManaHandler();
        if (handler == null) return 0;
        return handler.extractMana(amount, mana, false);
    }

    public boolean hasMana(Mana mana) {
        IManaHandler handler = getManaHandler();
        if (handler == null) return false;
        return handler.hasMana(mana);
    }

    public ManaNetworkNodeType getNodeType() {
        return nodeType;
    }

    public boolean setNodeType(ManaNetworkNodeType type) {
        boolean flag = false;
        if (manaNetwork != null && type != nodeType) {
            this.nodeType = type;
            manaNetwork.removeNode(this);
            manaNetwork.addNode(this);
            flag = true;
        }
        return flag;
    }

    public void provideMana(int amount, Mana mana) {
        if (manaNetwork != null && amount > 0) {
            manaNetwork.provideMana(new ManaTransaction(this, mana, amount));
        }
    }

    public void requestMana(int amount, Mana mana) {
        if (manaNetwork != null && amount > 0) {
            manaNetwork.requestMana(new ManaTransaction(this, mana, amount));
        }
    }

    public BlockPos getBlockPos() {
        return blockEntity.getBlockPos();
    }

    public void setChanged() {
        ((IManaNetworkSubscriber) blockEntity).markUpdated();
    }

    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        if (manaNetwork != null) tag.putUUID("mana_network", manaNetwork.getId());
        tag.putInt("priority", priority);
        tag.putInt("type", nodeType.ordinal());
        tag.put("connected_nodes", LibUtils.encode(BlockPos.CODEC.listOf(),
                connectedNodes.stream().map(i -> i.blockEntity.getBlockPos()).toList(),
                registries));
        tag.putBoolean("should_extract_when_saturated", shouldExtractWhenSaturated);
        return tag;
    }

    public void onRemoved(ServerLevel level) {
        if (manaNetwork != null) {
            manaNetwork.splitNetwork(this, level);
        }
    }

    public boolean shouldExtractWhenSaturated() {
        return shouldExtractWhenSaturated;
    }

    public @Nullable IManaHandler getManaHandler() {
        if (blockEntity.getLevel() == null) return null;
        return blockEntity.getLevel()
                .getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK, getBlockPos(), null);
    }
}