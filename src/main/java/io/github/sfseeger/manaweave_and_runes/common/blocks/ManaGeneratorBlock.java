package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.common.blocks.ManaNetworkBlock;
import io.github.sfseeger.manaweave_and_runes.client.particles.mana_particle.ManaParticleOptions;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaGeneratorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.menus.ManaGeneratorMenu;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.util.InventoryUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ManaGeneratorBlock extends ManaNetworkBlock implements EntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape SHAPE = Stream.of(
            Shapes.box(0.4375, 0.625, 0.4375, 0.5625, 0.8125, 0.5625),
            Shapes.box(0.0625, 0.063125, 0.0625, 0.9375, 0.813125, 0.9375),
            Shapes.box(0.125, 0.8125625, 0.125, 0.875, 0.9375625, 0.875),
            Shapes.box(0.125, 0.4375, 0.125, 0.875, 0.4375, 0.875),
            Shapes.box(0.25, 0.9375, 0.25, 0.75, 1, 0.75),
            Shapes.box(0.3125, 1, 0.3125, 0.6875, 1.0625, 0.6875),
            Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.0625, 0.9375),
            Shapes.box(0.125, 0.8125625, 0.125, 0.875, 0.9375625, 0.875),
            Shapes.box(0.0625, 0.0006249999999999867, 0.0625, 0.9375, 0.813125, 0.9375),
            Shapes.box(0.25, 0.9375, 0.25, 0.75, 1, 0.75),
            Shapes.box(0.3125, 1, 0.3125, 0.6875, 1.0625, 0.6875)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public ManaGeneratorBlock() {
        super(BlockBehaviour.Properties.of()
                      .sound(SoundType.METAL)
                      .strength(2.0f, 4.0f)
                      .requiresCorrectToolForDrops()
                      .lightLevel(state -> state.getValue(LIT) ? 13 : 0)
                      .noOcclusion());
        registerDefaultState(stateDefinition.any()
                                     .setValue(FACING, Direction.NORTH)
                                     .setValue(LIT, false)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaGeneratorBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                            BlockEntityType<T> blockEntityType
    ) {
        if (blockEntityType == MRBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get()) {
            if (!level.isClientSide) {
                return (level1, blockPos, blockState, blockEntity) -> ManaGeneratorBlockEntity.serverTick(level1,
                                                                                                          blockPos,
                                                                                                          blockState,
                                                                                                          (ManaGeneratorBlockEntity) blockEntity);
            }
        }
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult
    ) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(Objects.requireNonNull(state.getMenuProvider(level, pos)), pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ManaGeneratorBlockEntity entity) {
            return new SimpleMenuProvider(
                    (id, playerInventory, player) -> new ManaGeneratorMenu(id, playerInventory, entity,
                                                                           ContainerLevelAccess.create(level,
                                                                                                       pos)),
                    Component.translatable("container.manaweave_and_runes.mana_generator"));
        }
        return null;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        InventoryUtil.dropContentsOnDestroy(state, newState, level, pos,
                                            MRBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get());
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            double d0 = (double) pos.getX() + 0.5;
            double d1 = (double) pos.getY() + 0.1;
            double d2 = (double) pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                level.playLocalSound(d0, d1, d2, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F,
                                     false);
            }

            level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0);
            level.getBlockEntity(pos, MRBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get()).ifPresent(entity -> {
                IItemHandler itemHandler = entity.getItemHandler(null);
                if (!itemHandler.getStackInSlot(0).isEmpty()) {
                    ManaGeneratorBlockEntity.getManaMapData(itemHandler.getStackInSlot(0)).ifPresent(manaMapData -> {
                        manaMapData.manaMap().forEach((manaType , amount)-> {
                            int color = manaType.properties().getColor();
                            float r = (color >> 16) / 255.0f;
                            float g = (color >> 8) / 255.0f;
                            float b = color / 255.0f;

                            level.addParticle(new ManaParticleOptions(Math.min(random.nextFloat(), 0.5f), r, g, b, -0.1f,
                                                                      random.nextFloat() + 0.8f),
                                              d0 + random.nextFloat() - 0.5, d1 + 0.3, d2 + random.nextFloat() - 0.5, 0.0,
                                              0.0, 0.0);
                        });
                    });
                }
            });
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, LIT);
    }
}
