package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaHandler;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNode;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNodeType;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ManaGeneratorBlockEntity extends BlockEntity implements IManaNetworkSubscriber {
    public static final int CAPACITY = 1000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;

    private final ManaHandler manaHandler;
    private ManaNetworkNode manaNetworkNode = new ManaNetworkNode(this, ManaNetworkNodeType.PROVIDER);

    public ManaGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get(), pos, blockState);
        manaHandler =
                new ManaHandler(CAPACITY, MAX_EXTRACT, MAX_RECEIVE, null) {
                    @Override
                    public void onContentChanged() {
                        setChanged();
                    }
                };
    }

    @SuppressWarnings("unchecked")
    @Override
    public IManaHandler getManaHandler(@Nullable Direction side) {
        return this.manaHandler;
    }

    @Override
    public ManaNetworkNode getManaNetworkNode() {
        return manaNetworkNode;
    }

    @Override
    public void setManaNetworkNode(ManaNetworkNode node) {
        manaNetworkNode = node;
    }

    @Override
    public void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("mana")) manaHandler.deserializeNBT(registries, tag.getCompound("mana"));
        manaNetworkNode = ManaNetworkNode.deserializeNBT(tag.getCompound("mana_network_node"), registries, this)
                .orElse(new ManaNetworkNode(this, ManaNetworkNodeType.PROVIDER));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("mana", manaHandler.serializeNBT(registries));
        tag.put("mana_network_node",
                manaNetworkNode != null ? manaNetworkNode.serializeNBT(registries) : new CompoundTag());
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (manaNetworkNode != null) {
            manaNetworkNode.updateNetwork();
            manaNetworkNode.connectPendingNodes();
        }
    }
}
