package io.github.sfseeger.manaweave_and_runes.datagen.server.loot_tables;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MRLootTableProvider extends LootTableProvider {
    public MRLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                      new LootTableProvider.SubProviderEntry(MRBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
              registries);
    }
}
