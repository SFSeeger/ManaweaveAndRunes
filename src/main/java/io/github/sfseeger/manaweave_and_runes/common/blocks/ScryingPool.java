package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.common.rituals.marks.MarkDataAttachment;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataAttachmentInit;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ScryingPool extends Block {
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    public static final VoxelShape SHAPE =
            Stream.of(Block.box(2, 0, 2, 14, 2, 14),
                      Block.box(2, 2, 2, 14, 8, 4),
                      Block.box(2, 2, 12, 14, 8, 14),
                      Block.box(2, 2, 4, 4, 8, 12),
                      Block.box(12, 2, 4, 14, 8, 12))
                    .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();


    public ScryingPool() {
        super(Properties.of().strength(1.5f).noOcclusion().sound(SoundType.STONE));
        registerDefaultState(getStateDefinition().any().setValue(FILLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof ItemEntity itemEntity && itemEntity.getItem().is(Items.ECHO_SHARD) && !state.getValue(
                FILLED)) {
            state = state.setValue(FILLED, true);
            level.setBlockAndUpdate(pos, state);
            entity.kill();
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(FILLED) || !stack.is(Items.ECHO_SHARD)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
        stack.consume(1, player);
        state = state.setValue(FILLED, true);
        level.setBlockAndUpdate(pos, state);
        return ItemInteractionResult.CONSUME;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide()) {
            if (!state.getValue(FILLED)) return super.useWithoutItem(state, level, pos, player, hitResult);
            MarkDataAttachment attachment = player.getData(MRDataAttachmentInit.MARKS_DATA_ATTACHMENT_TYPE);
            player.displayClientMessage(Component.translatable("message.manaweave_and_runes.scrying_pool.intro"), false);
            if (attachment.getMarks().isEmpty()) {
                player.displayClientMessage(Component.translatable("message.manaweave_and_runes.scrying_pool.no_marks"),
                                            false);
            } else {
                MutableComponent message = Component.empty();
                for (MarkInstance mark : attachment.getCurses()) {
                    message.append(
                                    Component.translatable("message.manaweave_and_runes.scrying_pool.curse_entry", mark.getName()))
                            .append("\n");
                }
                for (MarkInstance mark : attachment.getBoons()) {
                    message.append(
                                    Component.translatable("message.manaweave_and_runes.scrying_pool.boon_entry", mark.getName()))
                            .append("\n");
                }
                player.displayClientMessage(message, false);
                level.playSound(player, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0f, 1.0f);
            }

        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FILLED, false);
    }
}
