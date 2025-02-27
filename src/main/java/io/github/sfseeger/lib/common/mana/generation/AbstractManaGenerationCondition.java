package io.github.sfseeger.lib.common.mana.generation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public abstract class AbstractManaGenerationCondition {
    public abstract int getManaGenerationPotential(Level level, BlockPos pos, BlockState state);
}
