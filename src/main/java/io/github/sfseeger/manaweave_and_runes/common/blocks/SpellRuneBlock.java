package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.manaweave_and_runes.common.blockentities.SpellRuneBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.util.ICreativeTabItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpellRuneBlock extends HalfTransparentBlock implements EntityBlock, ICreativeTabItem {
    public static final BooleanProperty HIDDEN = BooleanProperty.create("hidden");
    private static final VoxelShape VOXEL_SHAPE = Shapes.box(0f, 0f, 0f, 1f, 0.1f, 1f);


    public SpellRuneBlock() {
        super(Properties.of().sound(SoundType.CALCITE).noOcclusion().instabreak().noLootTable());
        registerDefaultState(getStateDefinition().any().setValue(HIDDEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HIDDEN);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(HIDDEN, false);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return VOXEL_SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        level.getBlockEntity(pos, MRBlockEntityInit.SPELL_RUNE_BLOCK_ENTITY.get()).ifPresent(blockEntity -> {
            if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
                blockEntity.onCast(livingEntity);
            }
        });
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SpellRuneBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, MRBlockEntityInit.SPELL_RUNE_BLOCK_ENTITY.get(),
                                  SpellRuneBlockEntity::tick);
    }

    @SuppressWarnings("unchecked")
    private static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public boolean isInCreativeTab() {
        return false;
    }
}
