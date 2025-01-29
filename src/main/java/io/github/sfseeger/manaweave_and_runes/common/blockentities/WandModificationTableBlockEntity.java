package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.spells.ISpellCaster;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class WandModificationTableBlockEntity extends BlockEntity {
    private final ItemStackHandler wandStackHandler = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof ISpellCaster;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            WandModificationTableBlockEntity.this.markUpdated();
        }
    };

    private void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public WandModificationTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.WAND_MODIFICATION_TABLE_BLOCK_ENTITY.get(), pos, blockState);
    }

    public ItemStackHandler getItemHandler(@Nullable Direction side) {
        return wandStackHandler;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        wandStackHandler.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", wandStackHandler.serializeNBT(registries));
    }

    public void dropContents() {
        for (int i = 0; i < wandStackHandler.getSlots(); i++) {
            Block.popResource(level, worldPosition, wandStackHandler.getStackInSlot(i));
        }
    }
}
