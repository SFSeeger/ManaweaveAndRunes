package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.mana.capability.ManaHandler;
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

public class ManaStorageBlockEntity extends BlockEntity {
    public static final int MANA_SLOTS = 5;
    ManaHandler manaHandler;

    public ManaStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(), pos, blockState);
        manaHandler = new ManaHandler(1000, 1000, 1000, MANA_SLOTS, null) {
            @Override
            public void onContentChanged() {
                setChanged();
            }
        };
    }

    public ManaHandler getManaHandler(Direction side) {
        return this.manaHandler;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("mana")) manaHandler.deserializeNBT(registries, tag.getCompound("mana"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("mana", manaHandler.serializeNBT(registries));
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
}
