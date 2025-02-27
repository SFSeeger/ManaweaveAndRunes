package io.github.sfseeger.lib.common.mana.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.LibUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class ManaNetworkHandler extends SavedData {
    private static final Logger logger = Logger.getLogger(ManaNetworkHandler.class.getName());

    public static final Codec<ManaNetworkHandler> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.listOf().orElse(List.of()).fieldOf("networks").forGetter(ManaNetworkHandler::getNetworkIds))
            .apply(instance, ManaNetworkHandler::fromCodec));

    public final Map<UUID, ManaNetwork> manaNetworks = new HashMap<>();
    public final Map<BlockPos, UUID> networkAtPos = new HashMap<>();

    public static ManaNetworkHandler getInstance(DimensionDataStorage storage) {
        return storage.computeIfAbsent(new Factory<>(ManaNetworkHandler::create, ManaNetworkHandler::load),
                                       "mana_networks");
    }

    private static ManaNetworkHandler create() {
        return new ManaNetworkHandler();
    }

    private static ManaNetworkHandler load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return CODEC.parse(NbtOps.INSTANCE, compoundTag.getCompound("mana_network_handler"))
                .result()
                .orElse(new ManaNetworkHandler());
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide()) return;
        ManaNetworkHandler.getInstance(((ServerLevel) event.getLevel()).getDataStorage()).manaNetworks.values()
                .forEach(ManaNetwork::tick);
    }

    public static ManaNetworkHandler fromCodec(List<String> networks) {
        ManaNetworkHandler handler = new ManaNetworkHandler();
        networks.forEach(id -> handler.manaNetworks.put(UUID.fromString(id), new ManaNetwork(UUID.fromString(id))));
        return handler;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.put("mana_network_handler", LibUtils.encode(CODEC, this, provider));
        logger.info("Saving mana networks");
        return compoundTag;
    }

    public void addNetwork(ManaNetwork network) {
        manaNetworks.put(network.getId(), network);
        setDirty();
    }

    public void removeNetwork(ManaNetwork network) {
        manaNetworks.remove(network.getId());
        setDirty();
    }

    public ManaNetwork createNetwork() {
        ManaNetwork network = new ManaNetwork(null);
        addNetwork(network);
        return network;
    }

    public List<String> getNetworkIds() {
        // Only return networks that have nodes
        return manaNetworks.entrySet().stream().filter(entry -> !entry.getValue().nodes.isEmpty())
                .map(entry -> entry.getKey().toString()).toList();
    }
}
