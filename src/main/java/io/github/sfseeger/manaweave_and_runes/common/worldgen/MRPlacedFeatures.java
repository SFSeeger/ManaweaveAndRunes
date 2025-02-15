package io.github.sfseeger.manaweave_and_runes.common.worldgen;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class MRPlacedFeatures {
    public static final ResourceKey<PlacedFeature> CRYSTAL_ORE_PLACED_KEY = registerKey("tanzanite_ore_placed");


    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, CRYSTAL_ORE_PLACED_KEY,
                 configuredFeatures.getOrThrow(MRConfiguredFeatures.OVERWORLD_TANZANITE_ORE_KEY),
                 MROrePlacements.commonOrePlacement(14, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                                                                                     VerticalAnchor.absolute(-4))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                                  ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> configuration,
            List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
