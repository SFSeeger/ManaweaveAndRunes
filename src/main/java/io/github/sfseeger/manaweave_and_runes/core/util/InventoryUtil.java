package io.github.sfseeger.manaweave_and_runes.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class InventoryUtil {
    public static void shrinkStackIfSurvival(Player player, ItemStack stack, int amount) {
        if (!player.isCreative()) {
            stack.shrink(amount);
        }
    }

    public static <T extends BlockEntityType<?>> void dropContentsOnDestroy(BlockState state, BlockState newState,
            Level level, BlockPos pos, T blockEntityType) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null && blockEntity.getType() == blockEntityType && blockEntity instanceof IInventoryBlockEntity be) {
                be.dropContentsOnDestroy(level, pos);
            }
        }
    }

    public static void dropContentsOnDestroy(BlockState state, BlockState newState, Level level, BlockPos pos) {
        if (!state.is(newState.getBlock())) {
            IItemHandler inventory = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
            if (inventory != null) {
                dropItemHandlerContents(inventory, level, pos);
            }
        }
    }

    public static void dropItemHandlerContents(final IItemHandler inventory, Level level, BlockPos pos) {
        if (inventory == null) return;
        for (int i = 0; i < inventory.getSlots(); ++i) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
        }
    }
}
