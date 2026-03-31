package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.common.blocks.ManaNetworkBlock;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaTransmitterBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.util.Utils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.atomic.AtomicReference;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManaTransmitter extends ManaNetworkBlock implements EntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    private static final VoxelShape SHAPE =
            Shapes.join(Block.box(5, 0, 5, 11, 13, 11), Block.box(6, 13, 6, 10, 14, 10), BooleanOp.OR);

    public ManaTransmitter() {
        super(BlockBehaviour.Properties.of()
                      .strength(0.5f)
                      .sound(SoundType.AMETHYST)
                      .requiresCorrectToolForDrops()
                      .lightLevel((blockState) -> 5));

        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Utils.rotateShape(state.getValue(FACING), SHAPE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaTransmitterBlockEntity(blockPos, blockState);
    }
}
