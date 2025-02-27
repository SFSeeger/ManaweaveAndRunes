package io.github.sfseeger.lib.common.mana.generation.builtIn;

import io.github.sfseeger.lib.common.mana.generation.AbstractManaGenerationCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.function.BiFunction;

public class HeightGenerationCondition extends AbstractManaGenerationCondition {
    private final int height;
    private final int divider;
    private final Comparison comparison;

    public HeightGenerationCondition(int height, int divider, Comparison comparison) {
        this.height = height;
        this.divider = divider;
        this.comparison = comparison;
    }

    @Override
    public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
        return comparison.compare(pos.getY(), height) ? pos.getY() / divider : 0;
    }

    public enum Comparison {
        GREATER_THAN((a, b) -> a > b),
        LESS_THAN((a, b) -> a < b),
        EQUAL_TO(Objects::equals);

        private final BiFunction<Integer, Integer, Boolean> function;

        Comparison(BiFunction<Integer, Integer, Boolean> function) {
            this.function = function;
        }

        public boolean compare(int a, int b) {
            return function.apply(a, b);
        }
    }
}
