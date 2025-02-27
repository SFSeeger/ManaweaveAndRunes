package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.spells.IUpgradable;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.util.IInventoryBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class RunewroughtBenchBlockEntity extends BlockEntity implements IInventoryBlockEntity {
    private final ItemStackHandler wandStackHandler = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof IUpgradable;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            RunewroughtBenchBlockEntity.this.markUpdated();
        }
    };

    private void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public RunewroughtBenchBlockEntity(BlockPos pos, BlockState blockState) {
        super(MRBlockEntityInit.RUNEWROUGHT_BENCH_BLOCK_ENTITY.get(), pos, blockState);
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
        InventoryUtil.dropItemHandlerContents(getItemHandler(null), level, worldPosition);
    }
}
