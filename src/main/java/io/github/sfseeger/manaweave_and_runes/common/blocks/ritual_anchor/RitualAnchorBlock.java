package io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor;

import io.github.sfseeger.manaweave_and_runes.common.blockentities.RitualAnchorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class RitualAnchorBlock extends Block implements EntityBlock {
    public final RitualAnchorType ritualAnchorType;

    public RitualAnchorBlock(RitualAnchorType type) {
        super(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST));
        this.ritualAnchorType = type;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RitualAnchorBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ManaweaveAndRunesBlockEntityInit.RITUAL_ANCHOR_BLOCK_ENTITY.get()) {
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
