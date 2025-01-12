package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
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

public class ManaTransmitterBlockEntity extends BlockEntity implements IManaNetworkSubscriber {
    private ManaNetworkNode node = new ManaNetworkNode(this, ManaNetworkNodeType.NONE);

    public ManaTransmitterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_TRANSMITTER_BLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    public <T extends IManaHandler> T getManaHandler(@Nullable Direction side) {
        return null;
    }

    @Override
    public ManaNetworkNode getManaNetworkNode() {
        return node;
    }

    @Override
    public void setManaNetworkNode(ManaNetworkNode node) {
        this.node = node;
    }

    @Override
    public void markUpdated() {
        setChanged();
        if (level != null)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        node = ManaNetworkNode.deserializeNBT(tag.getCompound("mana_network_node"), registries, this)
                .orElse(new ManaNetworkNode(this, ManaNetworkNodeType.NONE));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("mana_network_node", node.serializeNBT(registries));
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
        if (node != null) {
            node.updateNetwork();
            node.connectPendingNodes();
            markUpdated();
        }
    }
}
