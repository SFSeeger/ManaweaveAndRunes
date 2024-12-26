package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.ManaConcentratorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.ManaConcentratorType;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ManaConcentratorBlockEntity extends BlockEntity {
    private boolean isActive;

    public ManaConcentratorBlockEntity(BlockPos pos, BlockState state) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_CONCENTRATOR_BLOCK_ENTITY.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state,
            ManaConcentratorBlockEntity blockEntity) {
        if (level.getGameTime() % 20 == 0) {
            blockEntity.setActive(blockEntity.validateMultiblock().isValid());
            blockEntity.markUpdated();
        }
    }

    public MultiblockValidator.MultiBlockValidationData validateMultiblock() {
        if (level == null) {
            return new MultiblockValidator.MultiBlockValidationData(false, null, null, null);
        }
        ManaConcentratorType type = level.getBlockState(getBlockPos()).getBlock() instanceof ManaConcentratorBlock block
                ? block.getType()
                : null;
        if (type == null || type.getShapeValidator((ServerLevel) level) == null) {
            return new MultiblockValidator.MultiBlockValidationData(false, null, null, null);
        }
        return type.validate(level, getBlockPos());
    }

    private void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), ManaConcentratorBlock.UPDATE_ALL);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
