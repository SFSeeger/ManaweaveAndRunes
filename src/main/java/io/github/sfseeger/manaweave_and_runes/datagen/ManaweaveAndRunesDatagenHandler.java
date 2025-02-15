package io.github.sfseeger.manaweave_and_runes.datagen;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.datagen.client.ManaweaveAndRunesBlockStateProvider;
import io.github.sfseeger.manaweave_and_runes.datagen.client.ManaweaveAndRunesItemModelProvider;
import io.github.sfseeger.manaweave_and_runes.datagen.client.ManaweaveAndRunesParticleDescriptionProvider;
import io.github.sfseeger.manaweave_and_runes.datagen.server.MRAdvancementProvider;
import io.github.sfseeger.manaweave_and_runes.datagen.server.MRDataMapProvider;
import io.github.sfseeger.manaweave_and_runes.datagen.server.MRDataPackProvider;
import io.github.sfseeger.manaweave_and_runes.datagen.server.MRRecipeProvider;
import io.github.sfseeger.manaweave_and_runes.datagen.server.loot_tables.MRBlockLootSubProvider;
import io.github.sfseeger.manaweave_and_runes.datagen.server.tags.MRBlockTagsProvider;
import io.github.sfseeger.manaweave_and_runes.datagen.server.tags.MRItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ManaweaveAndRunesDatagenHandler {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(),
                              new ManaweaveAndRunesBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(),
                              new ManaweaveAndRunesItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(),
                              new ManaweaveAndRunesParticleDescriptionProvider(output, existingFileHelper));


        // Server
        generator.addProvider(event.includeServer(), new MRRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
                                                                           List.of(new LootTableProvider.SubProviderEntry(
                                                                                   MRBlockLootSubProvider::new,
                                                                                   LootContextParamSets.BLOCK)),
                                                                           event.getLookupProvider()));

        BlockTagsProvider blockTagsProvider = new MRBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(),
                              new MRItemTagProvider(output, lookupProvider, blockTagsProvider.contentsGetter(),
                                                    existingFileHelper));


        generator.addProvider(event.includeServer(),
                              new MRAdvancementProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new MRDataMapProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new MRDataPackProvider(output, lookupProvider));
    }
}
