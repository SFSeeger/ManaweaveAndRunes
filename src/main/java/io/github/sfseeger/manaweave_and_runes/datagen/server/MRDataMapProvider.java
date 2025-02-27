package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.lib.common.datamaps.BlockHarmDataMap;
import io.github.sfseeger.lib.common.datamaps.BlockHealDataMap;
import io.github.sfseeger.lib.common.datamaps.ManaMapData;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.Manas;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
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
        createItemManaDataMap();

        Builder<BlockHarmDataMap, Block> harm_builder = builder(BlockHarmDataMap.BLOCK_BLOCK_HARM_DATA);
        addBlockHarmData(harm_builder, Blocks.STONE, Blocks.COBBLESTONE, 0.0f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.COBBLESTONE, Blocks.GRAVEL, 0.0f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.GRAVEL, Blocks.SAND, 0.0f, 0.9f);
        addBlockHarmData(harm_builder, Blocks.SAND, Blocks.CLAY, 0.0f, 0.9f);
        addBlockHarmData(harm_builder, Blocks.CLAY, Blocks.SAND, 0.0f, 0.9f);

        addBlockHarmData(harm_builder, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS,
                         1.0f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.POLISHED_BLACKSTONE, Blocks.BLACKSTONE, 1.0f, 0.8f);

        addBlockHarmData(harm_builder, Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS, 1.5f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.POLISHED_DEEPSLATE, Blocks.DEEPSLATE, 1.5f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE, 1.5f, 0.8f);

        Builder<BlockHealDataMap, Block> heal_builder = builder(BlockHealDataMap.BLOCK_BLOCK_HEAL_DATA);
        addBlockHealData(heal_builder, Blocks.COBBLESTONE, Blocks.STONE, 0.0f, 0.8f);
        addBlockHealData(heal_builder, Blocks.GRAVEL, Blocks.COBBLESTONE, 0.0f, 0.8f);
        addBlockHealData(heal_builder, Blocks.SAND, Blocks.GRAVEL, 0.0f, 0.9f);
        addBlockHealData(heal_builder, Blocks.CLAY, Blocks.SAND, 0.0f, 0.9f);

        addBlockHealData(heal_builder, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS,
                         1.0f, 0.8f);
        addBlockHealData(heal_builder, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, 1.0f, 0.8f);

        addBlockHealData(heal_builder, Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICKS, 1.5f, 0.8f);
        addBlockHealData(heal_builder, Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE, 1.5f, 0.8f);
        addBlockHealData(heal_builder, Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE, 1.5f, 0.8f);
    }

    private void addBlockHealData(Builder<BlockHealDataMap, Block> builder, Block block, Block convertedBlock,
            float strength, float chance) {
        builder.add(block.builtInRegistryHolder(), new BlockHealDataMap(convertedBlock, strength, chance), false);
    }

    private void addBlockHarmData(Builder<BlockHarmDataMap, Block> builder, Block block, Block convertedBlock,
            float strength, float chance) {
        builder.add(block.builtInRegistryHolder(), new BlockHarmDataMap(convertedBlock, strength, chance), false);
    }


    private void createItemManaDataMap() {
        AdvancedBuilder<ManaMapData, Item, ?> builder = builder(ManaMapData.MANA_MAP_DATA);

        addManaData(builder, Items.COAL, Map.of(Manas.FireMana, 25));
        addManaData(builder, Items.BLUE_DYE, Map.of(Manas.AirMana, 10));
        addManaData(builder, ItemTags.TERRACOTTA, Map.of(Manas.FireMana, 4, Manas.AirMana, 2));
        addManaData(builder, Items.WATER_BUCKET, Map.of(Manas.WaterMana, 40));
        addManaData(builder, Items.LAVA_BUCKET, Map.of(Manas.FireMana, 60));
        addManaData(builder, Items.DIAMOND, Map.of(Manas.OrderMana, 150, Manas.EarthMana, 75));
        addManaData(builder, Items.NETHERITE_INGOT, Map.of(Manas.EntropyMana, 200, Manas.FireMana, 80));
        addManaData(builder, Items.ENDER_PEARL, Map.of(Manas.VoidMana, 90, Manas.AirMana, 35));
        addManaData(builder, Items.SOUL_SAND, Map.of(Manas.SoulMana, 30, Manas.EntropyMana, 15));
        addManaData(builder, Items.GRASS_BLOCK, Map.of(Manas.EarthMana, 5, Manas.WaterMana, 3));
        addManaData(builder, Items.FEATHER, Map.of(Manas.AirMana, 2));
        addManaData(builder, Items.FLINT_AND_STEEL, Map.of(Manas.FireMana, 35, Manas.EntropyMana, 12));
        addManaData(builder, Items.SNOWBALL, Map.of(Manas.WaterMana, 3, Manas.AirMana, 3));
        addManaData(builder, Items.GOLDEN_APPLE, Map.of(Manas.OrderMana, 100, Manas.SoulMana, 70));
        addManaData(builder, Items.EMERALD, Map.of(Manas.OrderMana, 120, Manas.EarthMana, 60));
        addManaData(builder, Items.OBSIDIAN, Map.of(Manas.VoidMana, 80, Manas.EarthMana, 50));
        addManaData(builder, Items.REDSTONE, Map.of(Manas.EntropyMana, 30));
        addManaData(builder, Items.GLOWSTONE_DUST, Map.of(Manas.FireMana, 20, Manas.OrderMana, 10));
        addManaData(builder, Items.NETHER_STAR, Map.of(Manas.SoulMana, 250, Manas.OrderMana, 150));
        addManaData(builder, Items.BONE, Map.of(Manas.SoulMana, 15));
        addManaData(builder, Items.ROTTEN_FLESH, Map.of(Manas.EntropyMana, 10));
        addManaData(builder, Items.SLIME_BALL, Map.of(Manas.WaterMana, 15, Manas.EarthMana, 5));
        addManaData(builder, Items.HONEYCOMB, Map.of(Manas.OrderMana, 10, Manas.WaterMana, 10));
        addManaData(builder, Items.SUGAR, Map.of(Manas.OrderMana, 5));
        addManaData(builder, Items.CACTUS, Map.of(Manas.WaterMana, 8, Manas.EntropyMana, 4));
        addManaData(builder, Items.PAPER, Map.of(Manas.AirMana, 5));
        addManaData(builder, Items.LEATHER, Map.of(Manas.EarthMana, 12));
        addManaData(builder, Items.STRING, Map.of(Manas.AirMana, 8));
        addManaData(builder, Items.SPIDER_EYE, Map.of(Manas.EntropyMana, 15));
        addManaData(builder, Items.MAGMA_CREAM, Map.of(Manas.FireMana, 30, Manas.WaterMana, 15));
        addManaData(builder, Items.NAUTILUS_SHELL, Map.of(Manas.WaterMana, 40));
        addManaData(builder, Items.PHANTOM_MEMBRANE, Map.of(Manas.AirMana, 25));
        addManaData(builder, Items.TURTLE_SCUTE, Map.of(Manas.WaterMana, 30, Manas.EarthMana, 20));
        addManaData(builder, Items.DRAGON_BREATH, Map.of(Manas.VoidMana, 100, Manas.AirMana, 50));
        addManaData(builder, Items.ELYTRA, Map.of(Manas.AirMana, 200));
        addManaData(builder, Items.IRON_INGOT, Map.of(Manas.OrderMana, 50, Manas.EarthMana, 25));
        addManaData(builder, Items.COPPER_INGOT, Map.of(Manas.EarthMana, 20));
        addManaData(builder, Items.LAPIS_LAZULI, Map.of(Manas.WaterMana, 30, Manas.OrderMana, 15));
        addManaData(builder, Items.AMETHYST_SHARD, Map.of(Manas.OrderMana, 40, Manas.AirMana, 20));
        addManaData(builder, Items.BLAZE_ROD, Map.of(Manas.FireMana, 70));
        addManaData(builder, Items.GHAST_TEAR, Map.of(Manas.SoulMana, 80, Manas.AirMana, 40));
        addManaData(builder, Items.TOTEM_OF_UNDYING, Map.of(Manas.SoulMana, 300, Manas.OrderMana, 200));
    }

    private void addManaData(AdvancedBuilder<ManaMapData, Item, ?> builder, Item item, Map<Mana, Integer> manaValues) {
        builder.add(item.builtInRegistryHolder(), new ManaMapData(manaValues), false);
    }

    private void addManaData(AdvancedBuilder<ManaMapData, Item, ?> builder, TagKey<Item> tag,
            Map<Mana, Integer> manaValues) {
        builder.add(tag, new ManaMapData(manaValues), false);
    }
}
