package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.lib.common.mana.ManaMapData;
import io.github.sfseeger.lib.common.mana.Manas;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MRDataMapProvider extends DataMapProvider {
    public MRDataMapProvider(PackOutput packOutput,
            CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(ManaMapData.MANA_MAP_DATA)
                .add(Items.COAL.builtInRegistryHolder(),
                     new ManaMapData(Map.of(Manas.FireMana, 25)), false)
                .add(Items.BLUE_DYE.builtInRegistryHolder(),
                     new ManaMapData(Map.of(Manas.AirMana, 10)), false)
                .add(ItemTags.TERRACOTTA,
                     new ManaMapData(Map.of(Manas.FireMana, 4, Manas.AirMana, 2)), false)
        ;
    }
}
