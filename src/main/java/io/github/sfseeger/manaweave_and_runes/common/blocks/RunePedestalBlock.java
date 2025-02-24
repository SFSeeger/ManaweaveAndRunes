package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.common.blocks.ManaNetworkBlock;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RunePedestalBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RunePedestalBlock extends ManaNetworkBlock implements EntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(0, 0, 0, 1, 0.125, 1),
            Shapes.box(0.25, 0.125, 0.25, 0.75, 0.875, 0.75),
            Shapes.box(0.0625, 0.875, 0.0625, 0.9375, 1, 0.9375)
    );

    public RunePedestalBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(1.5F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE));
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RunePedestalBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RunePedestalBlockEntity runePedestal) {
                ItemStack stack = runePedestal.removeItem();
                if (!stack.isEmpty()) {
                    if (!player.addItem(stack)) {
                        player.drop(stack, false);
                    }
                    runePedestal.inventory.setStackInSlot(0, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RunePedestalBlockEntity runePedestal) {
                if (stack.is(MRItemInit.MANA_CONNECTOR)) {
                    if (runePedestal.toggleState()) {
                        player.displayClientMessage(Component.literal("New mode: " + runePedestal.node.getNodeType()), false);
                        return ItemInteractionResult.SUCCESS;
                    }
                    return ItemInteractionResult.FAIL;
                }
                if (runePedestal.placeItem(stack)) {
                    return ItemInteractionResult.SUCCESS;
                }
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == MRBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get()) {
            if (!level.isClientSide) {
                return (level1, blockPos, blockState, blockEntity) -> RunePedestalBlockEntity.serverTick(level1,
                        blockPos,
                        blockState,
                        (RunePedestalBlockEntity) blockEntity);
            }
            return (level1, blockPos, blockState, blockEntity) -> RunePedestalBlockEntity.clientTick(level1,
                    blockPos,
                    blockState,
                    (RunePedestalBlockEntity) blockEntity);
        }
        return null;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        InventoryUtil.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
