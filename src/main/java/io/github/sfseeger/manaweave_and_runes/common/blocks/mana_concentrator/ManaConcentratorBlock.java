package io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator;

import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaConcentratorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.util.InventoryUtil;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaConcentratorBlock extends Block implements EntityBlock {
    private final ManaConcentratorType type;

    public ManaConcentratorBlock(ManaConcentratorType type) {
        super(Properties.of()
                      .strength(1.5f)
                      .requiresCorrectToolForDrops()
                      .sound(SoundType.AMETHYST)); //TODO: Add sounds?
        this.type = type;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ManaConcentratorBlockEntity manaConcentratorBlockEntity) {
            return manaConcentratorBlockEntity.isActive() ? 15 : 0;
        }
        return 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        ManaConcentratorBlockEntity blockEntity = new ManaConcentratorBlockEntity(blockPos, blockState);
        return blockEntity;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntityType) {
        if (blockEntityType == MRBlockEntityInit.MANA_CONCENTRATOR_BLOCK_ENTITY.get()) {
            if (!level.isClientSide) {
                return (level1, blockPos, blockState, blockEntity) -> ManaConcentratorBlockEntity.serverTick(level1,
                                                                                                             blockPos,
                                                                                                             blockState,
                                                                                                             (ManaConcentratorBlockEntity) blockEntity);
            }
            return (level1, blockPos, blockState, blockEntity) -> ManaConcentratorBlockEntity.clientTick(level1,
                                                                                                         blockPos,
                                                                                                         blockState,
                                                                                                         (ManaConcentratorBlockEntity) blockEntity);
        }
        return null;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ManaConcentratorBlockEntity manaConcentratorBlockEntity) {
                player.addItem(manaConcentratorBlockEntity.removeItem());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide && !stack.isEmpty()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ManaConcentratorBlockEntity manaConcentratorBlockEntity) {
                if (manaConcentratorBlockEntity.isActive()) {
                    if (stack.is(Items.CARROT_ON_A_STICK)) {// TODO: Replace with crafting start item
                        manaConcentratorBlockEntity.startCrafting();
                        return ItemInteractionResult.SUCCESS;
                    }
                } else if (stack.is(MRItemInit.MANA_DEBUG_STICK_ITEM)) {
                    MultiblockValidator.MultiBlockValidationData validationData =
                            manaConcentratorBlockEntity.validateMultiblock();
                    if (validationData.isValid()) {
                        player.sendSystemMessage(Component.literal("Multiblock is isValid!"));
                    } else {
                        player.sendSystemMessage(Component.literal("Multiblock is invalid!"));
                        player.sendSystemMessage(Component.literal(
                                "Wrong Block at: " + validationData.errorLocation() + " Expected: " + validationData.expected() + " Found: " + validationData.currentBlock()));
                    }
                    return ItemInteractionResult.SUCCESS;
                }
                if (manaConcentratorBlockEntity.placeItem(stack)) {
                    stack.setCount(0);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public ManaConcentratorType getType() {
        return type;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ManaConcentratorBlockEntity be) {
                InventoryUtil.dropItemHandlerContents(be.getItemHandler(null), level, pos);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
