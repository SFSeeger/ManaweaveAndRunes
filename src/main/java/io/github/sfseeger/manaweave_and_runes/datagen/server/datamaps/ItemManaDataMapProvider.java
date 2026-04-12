package io.github.sfseeger.manaweave_and_runes.datagen.server.datamaps;

import io.github.sfseeger.lib.common.datamaps.ManaMapData;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.Manas;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Map;

public class ItemManaDataMapProvider implements IDataMapRegistrar {
    @Override
    public void register(DataMapProvider provider) {

        DataMapProvider.AdvancedBuilder<ManaMapData, Item, ?> builder = provider.builder(ManaMapData.MANA_MAP_DATA);

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

    private void addManaData(DataMapProvider.AdvancedBuilder<ManaMapData, Item, ?> builder, Item item, Map<Mana, Integer> manaValues) {
        builder.add(item.builtInRegistryHolder(), new ManaMapData(manaValues), false);
    }

    private void addManaData(DataMapProvider.AdvancedBuilder<ManaMapData, Item, ?> builder, TagKey<Item> tag,
                             Map<Mana, Integer> manaValues
    ) {
        builder.add(tag, new ManaMapData(manaValues), false);
    }

}
