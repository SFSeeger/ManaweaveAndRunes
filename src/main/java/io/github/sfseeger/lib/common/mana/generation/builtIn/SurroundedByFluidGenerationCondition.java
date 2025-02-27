package io.github.sfseeger.lib.common.mana.generation.builtIn;

import io.github.sfseeger.lib.common.mana.generation.AbstractManaGenerationCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class SurroundedByFluidGenerationCondition extends AbstractManaGenerationCondition {
    private final Fluid target;
    private final int manaPerSource;

    public SurroundedByFluidGenerationCondition(Fluid target, int manaPerSource) {
        this.target = target;
        this.manaPerSource = manaPerSource;
    }

    public SurroundedByFluidGenerationCondition(Fluid target) {
        this(target, 1);
    }

    public static int countSurroundingFluid(Level level, BlockPos pos, BlockState state, Fluid fluid) {
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos offset = pos.offset(x, -1, z);
                FluidState fluidState = level.getBlockState(offset).getFluidState();
                if (fluidState.isSource() && fluidState.getType().isSame(fluid)) {
                    count += 1;
                }
            }
        }
        return count;
    }

    @Override
    public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
        return countSurroundingFluid(level, pos, state, target) * manaPerSource;
    }
}
