package io.github.sfseeger.lib.mana.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ManaGenerationHelper {
    public static int countSurroundingFluid(Level level, BlockPos pos, BlockState state, Fluid fluid) {
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos offset = pos.offset(x, 0, z);
                FluidState fluidState = level.getBlockState(offset).getFluidState();
                if (fluidState.isSource() && fluidState.getType().isSame(fluid)) {
                    count += 1;
                }
            }
        }
        return count;
    }

    public enum GenerationConditionModi {
        OR,
        XOR,
        AND,
    }

    public enum GenerationCondition {
        RAINING {
            @Override
            public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
                return level.isRaining() ? 1 : 0;
            }
        },
        SURROUNDED_BY_WATER {
            @Override
            public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
                return countSurroundingFluid(level, pos, state, Fluids.WATER);
            }
        },
        SURROUNDED_BY_LAVA {
            @Override
            public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
                return countSurroundingFluid(level, pos, state, Fluids.LAVA);
            }
        },
        PLACED_HIGH {
            @Override
            public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
                return pos.getY() > 70 ? pos.getY() / 63 : 0;
            }
        };

        abstract public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state);
    }
}
