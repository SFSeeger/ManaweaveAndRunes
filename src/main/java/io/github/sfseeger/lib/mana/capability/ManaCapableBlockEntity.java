package io.github.sfseeger.lib.mana.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ManaCapableBlockEntity extends BlockEntity {
    protected ManaHandler manaHandler;

    public ManaCapableBlockEntity(BlockEntityType<?> type,
            BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public ManaHandler getManaHandler(@Nullable Direction side) {
        return manaHandler;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        manaHandler.deserializeNBT(registries, tag.getCompound("manaHandler"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("manaHandler", manaHandler.serializeNBT(registries));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }
}
