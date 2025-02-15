package io.github.sfseeger.manaweave_and_runes.common.worldgen;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MRBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_TANZANITE_ORE = registerKey("add_tanzanite_ore");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_TANZANITE_ORE,
                         new BiomeModifiers.AddFeaturesBiomeModifier(biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                                                                     HolderSet.direct(placedFeatures.getOrThrow(
                                                                             MRPlacedFeatures.CRYSTAL_ORE_PLACED_KEY)),
                                                                     GenerationStep.Decoration.UNDERGROUND_ORES));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                                  ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, name));
    }
}
