package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.common.blocks.ManaNetworkBlock;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaCollectorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.util.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class ManaCollectorBlock extends ManaNetworkBlock implements EntityBlock {
    private static final VoxelShape SHAPE = Stream.of(
            Block.box(7, 4, 14.5, 9, 12, 15.5),
            Block.box(7, 4, 0.5, 9, 12, 1.5),
            Block.box(14.5, 4, 7, 15.5, 12, 9),
            Block.box(0.5, 4, 7, 1.5, 12, 9),
            Block.box(3.5, 12, 1.5, 5.5, 14, 2.5),
            Block.box(0.5, 12, 5.5, 1.5, 14, 10.5),
            Block.box(14.5, 12, 5.5, 15.5, 14, 10.5),
            Block.box(2.5, 12, 2.5, 3.5, 14, 3.5),
            Block.box(1.5, 12, 3.5, 2.5, 14, 5.5),
            Block.box(1.5, 12, 10.5, 2.5, 14, 12.5),
            Block.box(2.5, 12, 12.5, 3.5, 14, 13.5),
            Block.box(13.5, 12, 10.5, 14.5, 14, 12.5),
            Block.box(12.5, 12, 12.5, 13.5, 14, 13.5),
            Block.box(12.5, 12, 2.5, 13.5, 14, 3.5),
            Block.box(13.5, 12, 3.5, 14.5, 14, 5.5),
            Block.box(10.5, 12, 13.5, 12.5, 14, 14.5),
            Block.box(5.5, 12, 14.5, 10.5, 14, 15.5),
            Block.box(3.5, 12, 13.5, 5.5, 14, 14.5),
            Block.box(10.5, 12, 1.5, 12.5, 14, 2.5),
            Block.box(5.5, 12, 0.5, 10.5, 14, 1.5),
            Block.box(0, 0, 0, 16, 4, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public ManaCollectorBlock() {
        super(Properties.of().strength(2.5f).requiresCorrectToolForDrops().randomTicks());
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof ManaCollectorBlockEntity manaCollectorBlockEntity
                && manaCollectorBlockEntity.isCollecting() ? 15 : 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaCollectorBlockEntity(blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ManaCollectorBlockEntity manaCollectorBlockEntity) {
                ItemStack stack = manaCollectorBlockEntity.removeRune();
                if (stack.isEmpty()) {
                    return InteractionResult.FAIL;
                }
                if (!player.addItem(stack.copy())) {
                    ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 1.2f, pos.getZ() + 0.5f,
                                                       stack.copy());
                    entity.setDefaultPickUpDelay();
                    level.addFreshEntity(entity);
                    stack.setCount(0);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ManaCollectorBlockEntity manaCollectorBlockEntity && manaCollectorBlockEntity.placeRune(
                    player, stack)) {
                stack.shrink(1);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof ManaCollectorBlockEntity be) {
            ManaCollectorBlockEntity.serverTick(level, pos, state, be);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        InventoryUtil.dropContentsOnDestroy(state, newState, level, pos,
                                            MRBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get());
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(
                pos) instanceof ManaCollectorBlockEntity manaCollectorBlockEntity && manaCollectorBlockEntity.isCollecting()) {

        }
    }


}
