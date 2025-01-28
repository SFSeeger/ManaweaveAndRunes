package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.spells.ISpellCaster;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class WandModificationTableBlockEntity extends BlockEntity {
    private ItemStackHandler handler = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof ISpellCaster;
        }
    };

    public WandModificationTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.WAND_MODIFICATION_TABLE_BLOCK_ENTITY.get(), pos, blockState);
    }

    public ItemStackHandler getItemHandler(@Nullable Direction side) {
        return handler;
    }
}
