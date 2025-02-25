package io.github.sfseeger.lib.common.mana.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

public class ManaGenerationHelper {
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

    public static int countSurroundingBlocks(Level level, BlockPos pos, BlockState state,
            Either<Block, TagKey<Block>> target) {
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos offset = pos.offset(x, -1, z);
                BlockState blockState = level.getBlockState(offset);
                count += target.map(
                        block -> blockState.is(block) ? 1 : 0,
                        tag -> blockState.is(tag) ? 1 : 0
                );
            }
        }
        return count;
    }

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
        IN_HOT_BIOME {
            @Override
            public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
                return level.getBiome(pos).is(Tags.Biomes.IS_HOT) ? 2 : 0;
            }
        },
        IN_COLD_BIOME {
            @Override
            public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
                return level.getBiome(pos).is(Tags.Biomes.IS_COLD) ? 2 : 0;
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
