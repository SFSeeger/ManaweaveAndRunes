package io.github.sfseeger.lib.common.mana.generation;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ManaGenerationHelper {
    public enum GenerationConditionModi {
        OR,
        XOR,
        AND;

        public static Codec<GenerationConditionModi> CODEC = Codec.STRING.xmap(
                GenerationConditionModi::valueOf,
                GenerationConditionModi::name
        );
    }

    public enum GenerationCondition {
        RAINING {
            @Override
            public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
                return level.isRaining() ? 1 : 0;
            }
        },
        PLACED_HIGH {
            @Override
            public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
                return pos.getY() > 70 ? pos.getY() / 31 : 0;
            }
        },
        PLACED_LOW {
            @Override
            public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
                return pos.getY() < 10 ? pos.getY() / 8 : 0;
            }
        };

        public static Codec<GenerationCondition> CODEC = Codec.STRING.xmap(
                GenerationCondition::valueOf,
                GenerationCondition::name
        );

        abstract public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state);
    }
}
