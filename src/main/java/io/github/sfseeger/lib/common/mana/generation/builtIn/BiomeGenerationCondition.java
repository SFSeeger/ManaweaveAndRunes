package io.github.sfseeger.lib.common.mana.generation.builtIn;

import io.github.sfseeger.lib.common.mana.generation.AbstractManaGenerationCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class BiomeGenerationCondition extends AbstractManaGenerationCondition {
    private final TagKey<Biome> biomeTag;
    private final int manaGenerationPotential;

    public BiomeGenerationCondition(TagKey<Biome> biomeTag, int manaGenerationPotential) {
        this.biomeTag = biomeTag;
        this.manaGenerationPotential = manaGenerationPotential;
    }

    @Override
    public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
        return level.getBiome(pos).is(biomeTag) ? manaGenerationPotential : 0;
    }
}
