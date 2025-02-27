package io.github.sfseeger.manaweave_and_runes.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public interface IInventoryBlockEntity {
    public <T extends IItemHandler> T getItemHandler(@Nullable Direction side);

    default void dropContentsOnDestroy(Level level, BlockPos pos) {
        InventoryUtil.dropItemHandlerContents(getItemHandler(null), level, pos);
    }
}
