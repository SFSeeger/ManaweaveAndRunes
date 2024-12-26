package io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator;

import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaConcentratorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
        if (blockEntityType == ManaweaveAndRunesBlockEntityInit.MANA_CONCENTRATOR_BLOCK_ENTITY.get()) {
            if (!level.isClientSide) {
                return (level1, blockPos, blockState, blockEntity) -> ManaConcentratorBlockEntity.serverTick(level1,
                                                                                                             blockPos,
                                                                                                             blockState,
                                                                                                             (ManaConcentratorBlockEntity) blockEntity);
            }
        }
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ManaConcentratorBlockEntity manaConcentratorBlockEntity) {
                MultiblockValidator.MultiBlockValidationData validationData =
                        manaConcentratorBlockEntity.validateMultiblock();
                if (validationData.isValid()) {
                    player.sendSystemMessage(Component.literal("Multiblock is valid!"));
                } else {
                    player.sendSystemMessage(Component.literal("Multiblock is invalid!"));
                    player.sendSystemMessage(Component.literal(
                            "Wrong Block at: " + validationData.errorLocation() + " Expected: " + validationData.expectedBlock() + " Found: " + validationData.currentBlock()));
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ManaConcentratorBlockEntity manaConcentratorBlockEntity && manaConcentratorBlockEntity.isActive()) {
            level.addParticle(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 0, 0, 0);
        }
    }

    public ManaConcentratorType getType() {
        return type;
    }
}