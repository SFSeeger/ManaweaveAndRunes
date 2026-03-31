package io.github.sfseeger.manaweave_and_runes.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class RuneBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty RITUAL_ACTIVE = BooleanProperty.create("ritual_active");

    public RuneBlock() {
        super(BlockBehaviour.Properties.of()
                      .strength(1.5F)
                      .requiresCorrectToolForDrops()
                      .sound(SoundType.STONE));
        registerDefaultState(stateDefinition.any()
                                     .setValue(FACING, Direction.NORTH)
                                     .setValue(ACTIVE, false)
                                     .setValue(RITUAL_ACTIVE, false)
        );
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE, RITUAL_ACTIVE);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(ACTIVE) ? 8 : 0;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            boolean is_active = state.getValue(ACTIVE);
            if (is_active != level.hasNeighborSignal(pos) && !state.getValue(RITUAL_ACTIVE)) {
                if (is_active) {
                    level.scheduleTick(pos, this, 4);
                } else {
                    level.setBlock(pos, state.cycle(ACTIVE), 2);
                }
            }
        }
    }

    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVE) && !(level.hasNeighborSignal(pos) || state.getValue(RITUAL_ACTIVE))) {
            level.setBlock(pos, state.cycle(ACTIVE), 2);
        }
    }
}
