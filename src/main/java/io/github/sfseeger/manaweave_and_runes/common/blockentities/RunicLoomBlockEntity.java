package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class RunicLoomBlockEntity extends BlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2);

    public RunicLoomBlockEntity(BlockPos pos, BlockState blockState) {
        super(MRBlockEntityInit.RUNIC_LOOM_BLOCK_ENTITY.get(), pos, blockState);
    }

    public ItemStackHandler getItemHandler(@Nullable Direction side) {
        return itemHandler;
    }

}
