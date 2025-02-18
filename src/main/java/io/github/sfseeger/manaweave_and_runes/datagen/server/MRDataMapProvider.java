package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.lib.common.datamaps.BlockHarmDataMap;
import io.github.sfseeger.lib.common.datamaps.BlockHealDataMap;
import io.github.sfseeger.lib.common.datamaps.ManaMapData;
import io.github.sfseeger.lib.common.mana.Manas;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MRDataMapProvider extends DataMapProvider {
    public MRDataMapProvider(PackOutput packOutput,
            CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    @SuppressWarnings("deprecated")
    protected void gather() {
        builder(ManaMapData.MANA_MAP_DATA)
                .add(Items.COAL.builtInRegistryHolder(),
                     new ManaMapData(Map.of(Manas.FireMana, 25)), false)
                .add(Items.BLUE_DYE.builtInRegistryHolder(),
                     new ManaMapData(Map.of(Manas.AirMana, 10)), false)
                .add(ItemTags.TERRACOTTA,
                     new ManaMapData(Map.of(Manas.FireMana, 4, Manas.AirMana, 2)), false)
        ;

        builder(BlockHarmDataMap.BLOCK_BLOCK_HARM_DATA)
                .add(Blocks.STONE.builtInRegistryHolder(), new BlockHarmDataMap(Blocks.COBBLESTONE, 0.0f, 0.8f), false)
                .add(Blocks.COBBLESTONE.builtInRegistryHolder(), new BlockHarmDataMap(Blocks.GRAVEL, 0.0f, 0.8f), false)
                .add(Blocks.GRAVEL.builtInRegistryHolder(), new BlockHarmDataMap(Blocks.SAND, 0.0f, 0.9f), false)
                .add(Blocks.SAND.builtInRegistryHolder(), new BlockHarmDataMap(Blocks.CLAY, 0.0f, 0.9f), false)
                .add(Blocks.CLAY.builtInRegistryHolder(), new BlockHarmDataMap(Blocks.SAND, 0.0f, 0.9f), false)

                .add(Blocks.POLISHED_BLACKSTONE_BRICKS.builtInRegistryHolder(),
                     new BlockHarmDataMap(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 1.0f, 0.8f), false)
                .add(Blocks.POLISHED_BLACKSTONE.builtInRegistryHolder(),
                     new BlockHarmDataMap(Blocks.BLACKSTONE, 1.0f, 0.8f), false)

                .add(Blocks.DEEPSLATE_BRICKS.builtInRegistryHolder(),
                     new BlockHarmDataMap(Blocks.CRACKED_DEEPSLATE_BRICKS, 1.5f, 0.8f), false)
                .add(Blocks.POLISHED_DEEPSLATE.builtInRegistryHolder(),
                     new BlockHarmDataMap(Blocks.DEEPSLATE, 1.5f, 0.8f), false)
                .add(Blocks.DEEPSLATE.builtInRegistryHolder(),
                     new BlockHarmDataMap(Blocks.COBBLED_DEEPSLATE, 1.5f, 0.8f), false);

        builder(BlockHealDataMap.BLOCK_BLOCK_HEAL_DATA)
                .add(Blocks.COBBLESTONE.builtInRegistryHolder(), new BlockHealDataMap(Blocks.STONE, 0.0f, 0.8f), false)
                .add(Blocks.GRAVEL.builtInRegistryHolder(), new BlockHealDataMap(Blocks.COBBLESTONE, 0.0f, 0.8f), false)
                .add(Blocks.SAND.builtInRegistryHolder(), new BlockHealDataMap(Blocks.GRAVEL, 0.0f, 0.9f), false)
                .add(Blocks.CLAY.builtInRegistryHolder(), new BlockHealDataMap(Blocks.SAND, 0.0f, 0.9f), false)

                .add(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.builtInRegistryHolder(),
                     new BlockHealDataMap(Blocks.POLISHED_BLACKSTONE_BRICKS, 1.0f, 0.8f), false)
                .add(Blocks.BLACKSTONE.builtInRegistryHolder(),
                     new BlockHealDataMap(Blocks.POLISHED_BLACKSTONE, 1.0f, 0.8f), false)

                .add(Blocks.CRACKED_DEEPSLATE_BRICKS.builtInRegistryHolder(),
                     new BlockHealDataMap(Blocks.DEEPSLATE_BRICKS, 1.5f, 0.8f), false)
                .add(Blocks.DEEPSLATE.builtInRegistryHolder(),
                     new BlockHealDataMap(Blocks.POLISHED_DEEPSLATE, 1.5f, 0.8f), false)
                .add(Blocks.COBBLED_DEEPSLATE.builtInRegistryHolder(),
                     new BlockHealDataMap(Blocks.DEEPSLATE, 1.5f, 0.8f), false);
    }
}
