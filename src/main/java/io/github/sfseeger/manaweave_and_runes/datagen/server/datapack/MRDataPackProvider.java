package io.github.sfseeger.manaweave_and_runes.datagen.server.datapack;

import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.worldgen.MRBiomeModifiers;
import io.github.sfseeger.manaweave_and_runes.common.worldgen.MRConfiguredFeatures;
import io.github.sfseeger.manaweave_and_runes.common.worldgen.MRPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MRDataPackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, MRConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, MRPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, MRBiomeModifiers::bootstrap)
            .add(ManaweaveAndRunesRegistries.RITUAL_INPUT_REGISTRY_KEY, RitualInputs::bootsrap)
            .add(Registries.DAMAGE_TYPE, MRDamageTypeProvider::bootstrap);

    public MRDataPackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ManaweaveAndRunes.MODID));
    }
}
