package io.github.sfseeger.lib.common.mana;

import io.github.sfseeger.lib.common.mana.generation.builtIn.BiomeGenerationCondition;
import io.github.sfseeger.lib.common.mana.generation.builtIn.HeightGenerationCondition;
import io.github.sfseeger.lib.common.mana.generation.builtIn.SurroundedByBlockGenerationCondition;
import io.github.sfseeger.lib.common.mana.generation.builtIn.SurroundedByFluidGenerationCondition;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

public class Manas {
    public static final Mana EmptyMana = new Mana(new ManaProperties.Builder().canBeGenerated(false).build());

    public static final Mana FireMana = new Mana(
            new ManaProperties.Builder().color(0xFF0000)
                    .addGenerationCondition(new SurroundedByFluidGenerationCondition(Fluids.LAVA))
                    .addGenerationCondition(new BiomeGenerationCondition(Tags.Biomes.IS_HOT, 2))
                    .addGenerationCondition(new BiomeGenerationCondition(Tags.Biomes.IS_NETHER, 4))
                    .canBeGenerated(true)
                    .icon(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/mana/fire_mana"))
                    .build());

    public static final Mana AirMana = new Mana(
            new ManaProperties.Builder().color(0xADF3FF)
                    .addGenerationCondition(
                            new HeightGenerationCondition(70, 16, HeightGenerationCondition.Comparison.GREATER_THAN))
                    .canBeGenerated(true)
                    .icon(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/mana/air_mana"))
                    .build());

    public static final Mana WaterMana = new Mana(
            new ManaProperties.Builder().color(0x0955A8)
                    .addGenerationCondition(new SurroundedByFluidGenerationCondition(Fluids.WATER))
                    .addGenerationCondition(new BiomeGenerationCondition(Tags.Biomes.IS_OCEAN, 2))
                    .canBeGenerated(true)
                    .icon(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/mana/water_mana"))
                    .build());

    public static final Mana EarthMana = new Mana(
            new ManaProperties.Builder().color(0x16615B)
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Tags.Blocks.ORES_IRON, 1))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Tags.Blocks.ORES_GOLD, 2))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Tags.Blocks.ORES_DIAMOND, 3))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.ANCIENT_DEBRIS, 4))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.EMERALD_ORE, 4))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.DEEPSLATE_EMERALD_ORE, 5))
                    .canBeGenerated(true)
                    .icon(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/mana/earth_mana"))
                    .build());

    public static final Mana VoidMana = new Mana(
            new ManaProperties.Builder().color(0x2A2F4E)
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.BEDROCK, 3))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.WARPED_WART_BLOCK, 1))
                    .addGenerationCondition(new BiomeGenerationCondition(Tags.Biomes.IS_END, 2))
                    .canBeGenerated(true)
                    .icon(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/mana/void_mana"))
                    .build());

    public static final Mana SoulMana = new Mana(
            new ManaProperties.Builder().color(0xB0BDC5)
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Tags.Blocks.SKULLS, 2))
                    .canBeGenerated(true)
                    .icon(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/mana/soul_mana"))
                    .build());

    public static final Mana OrderMana = new Mana(
            new ManaProperties.Builder().color(0xFFB74D)
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.LODESTONE, 4))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.ENCHANTING_TABLE, 2))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.BEACON, 3))
                    .canBeGenerated(true)
                    .icon(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/mana/order_mana"))
                    .build());

    public static final Mana EntropyMana = new Mana(
            new ManaProperties.Builder().color(0x2A2F4E)
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.TNT, 1))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.CREEPER_HEAD, 3))
                    .addGenerationCondition(new SurroundedByBlockGenerationCondition(Blocks.SLIME_BLOCK, 1))
                    .canBeGenerated(true)
                    .icon(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/mana/entropy_mana"))
                    .build());

}
