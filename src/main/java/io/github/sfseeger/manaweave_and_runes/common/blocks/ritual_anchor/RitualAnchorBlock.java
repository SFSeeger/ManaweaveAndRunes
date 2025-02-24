package io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor;

import io.github.sfseeger.lib.common.blocks.ManaNetworkBlock;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RitualAnchorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class RitualAnchorBlock extends ManaNetworkBlock implements EntityBlock {
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(0, 0, 0, 16, 3, 16),
            Block.box(2, 3, 2, 14, 6, 14),
            Block.box(3, 6, 3, 13, 11, 13),
            Block.box(0, 11, 0, 15, 12, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public final RitualAnchorType ritualAnchorType;

    public RitualAnchorBlock(RitualAnchorType type) {
        super(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE));
        this.ritualAnchorType = type;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RitualAnchorBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntityType) {
        if (blockEntityType == MRBlockEntityInit.RITUAL_ANCHOR_BLOCK_ENTITY.get()) {
            if (!level.isClientSide) {
                return (level1, blockPos, blockState, blockEntity) -> RitualAnchorBlockEntity.serverTick(level1,
                                                                                                         blockPos,
                                                                                                         blockState,
                                                                                                         (RitualAnchorBlockEntity) blockEntity);
            }
            return (level1, blockPos, blockState, blockEntity) -> RitualAnchorBlockEntity.clientTick(level1,
                                                                                                     blockPos,
                                                                                                     blockState,
                                                                                                     (RitualAnchorBlockEntity) blockEntity);
        }
        return null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide && !stack.isEmpty()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RitualAnchorBlockEntity be) {
                return be.checkAndStartRitual(level, player,
                                              stack) ? ItemInteractionResult.SUCCESS : ItemInteractionResult.FAIL;
            }
        }

        return ItemInteractionResult.CONSUME;
    }
}
