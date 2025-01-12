package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.common.blocks.ManaNetworkBlock;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaTransmitterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ManaTransmitter extends ManaNetworkBlock implements EntityBlock {
    private static final VoxelShape SHAPE =
            Shapes.join(Block.box(5, 0, 5, 11, 13, 11), Block.box(6, 13, 6, 10, 14, 10), BooleanOp.OR);

    public ManaTransmitter() {
        super(BlockBehaviour.Properties.of()
                      .strength(0.5f)
                      .requiresCorrectToolForDrops()
                      .lightLevel((blockState) -> 5));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaTransmitterBlockEntity(blockPos, blockState);
    }
}
