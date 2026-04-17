package io.github.sfseeger.manaweave_and_runes.datagen.server.recipes;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.datagen.recipes.ManaConcentratorRecipeBuilder;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRTagInit;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ManaConcentratorRecipeProvider implements IRecipeRegistrar {
    @Override
    public void register(RecipeProvider provider, RecipeOutput output) {
        makeManaInfusedRockRecipe(output, MRBlockInit.AIR_MANA_INFUSED_ROCK_BLOCK,
                                  Ingredient.of(Items.FEATHER, Items.WIND_CHARGE), Manas.AirMana);
        makeManaInfusedRockRecipe(output, MRBlockInit.FIRE_MANA_INFUSED_ROCK_BLOCK,
                                  Ingredient.of(Items.COAL, Items.CHARCOAL), Manas.FireMana);
        makeManaInfusedRockRecipe(output, MRBlockInit.EARTH_MANA_INFUSED_ROCK_BLOCK, Ingredient.of(Items.STONE),
                                  Manas.EarthMana);
        makeManaInfusedRockRecipe(output, MRBlockInit.WATER_MANA_INFUSED_ROCK_BLOCK, Ingredient.of(Items.SAND),
                                  Manas.WaterMana);
        makeManaInfusedRockRecipe(output, MRBlockInit.ENTROPY_MANA_INFUSED_ROCK_BLOCK, Ingredient.of(Items.TNT),
                                  Manas.EntropyMana);
        makeManaInfusedRockRecipe(output, MRBlockInit.ORDER_MANA_INFUSED_ROCK_BLOCK,
                                  Ingredient.of(Items.REDSTONE), Manas.OrderMana);
        makeManaInfusedRockRecipe(output, MRBlockInit.SOUL_MANA_INFUSED_ROCK_BLOCK,
                                  Ingredient.of(Tags.Items.FOODS_RAW_MEAT), Manas.SoulMana);
        makeManaInfusedRockRecipe(output, MRBlockInit.VOID_MANA_INFUSED_ROCK_BLOCK,
                                  Ingredient.of(Items.OBSIDIAN, Items.CRYING_OBSIDIAN),
                                  Manas.VoidMana); //TODO: Change to cheaper item

        new ManaConcentratorRecipeBuilder.Builder(MRItemInit.MANA_WEAVERS_STAFF_ITEM).setTier(Tier.NOVICE)
                .setCraftTime(300)
                .addInput(Ingredient.of(Tags.Items.STRIPPED_WOODS))
                .addInput(Ingredient.of(Items.GOLD_INGOT))
                .addInput(Ingredient.of(MRItemInit.MANA_WEAVER_WAND_ITEM))
                .addInput(Ingredient.of(MRTagInit.SPELL_MANA_PROVIDER))
                .addMana(Manas.FireMana, 250)
                .addMana(Manas.AirMana, 250)
                .addMana(Manas.EarthMana, 250)
                .addMana(Manas.WaterMana, 250)
                .addMana(Manas.EntropyMana, 50)
                .addMana(Manas.OrderMana, 50)
                .addMana(Manas.SoulMana, 50)
                .addMana(Manas.VoidMana, 50)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRBlockInit.NOVICE_RITUAL_ANCHOR_BLOCK).setTier(Tier.NOVICE)
                .setCraftTime(350)
                .addInput(Ingredient.of(Items.AMETHYST_BLOCK))
                .addInput(Ingredient.of(Items.ENCHANTING_TABLE))
                .addInput(Ingredient.of(Items.HEART_OF_THE_SEA))
                .addInput(Ingredient.of(MRBlockInit.MANA_STORAGE_BLOCK))
                .addMana(Manas.FireMana, 500)
                .addMana(Manas.AirMana, 500)
                .addMana(Manas.EarthMana, 500)
                .addMana(Manas.WaterMana, 500)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK.get()).setTier(Tier.NOVICE)
                .setCraftTime(500)
                .addInput(Ingredient.of(Items.GOLD_BLOCK))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE))
                .addInput(Ingredient.of(Items.HEART_OF_THE_SEA))
                .addMana(Manas.FireMana, 500)
                .addMana(Manas.AirMana, 500)
                .addMana(Manas.EarthMana, 500)
                .addMana(Manas.WaterMana, 500)
                .addMana(Manas.EntropyMana, 100)
                .addMana(Manas.OrderMana, 100)
                .addMana(Manas.SoulMana, 100)
                .addMana(Manas.VoidMana, 100)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK.get()).setTier(
                        Tier.MASTER)
                .setCraftTime(500)
                .addInput(Ingredient.of(Items.EMERALD_BLOCK))
                .addInput(Ingredient.of(MRBlockInit.TANZANITE_BLOCK))
                .addInput(Ingredient.of(MRItemInit.RUNE_MATRIX_ITEM))
                .addInput(Ingredient.of(MRBlockInit.MANA_TRANSMITTER_BLOCK))
                .addInput(Ingredient.of(Items.NETHERITE_INGOT))
                .addInput(Ingredient.of(Items.END_CRYSTAL))
                .addInput(Ingredient.of(Items.END_STONE))
                .addInput(Ingredient.of(Items.END_STONE))
                .addMana(Manas.FireMana, 1000)
                .addMana(Manas.AirMana, 1000)
                .addMana(Manas.EarthMana, 1000)
                .addMana(Manas.WaterMana, 1000)
                .addMana(Manas.EntropyMana, 500)
                .addMana(Manas.OrderMana, 500)
                .addMana(Manas.SoulMana, 500)
                .addMana(Manas.VoidMana, 500)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRBlockInit.MASTER_RITUAL_ANCHOR_BLOCK.get())
                .setTier(Tier.MASTER)
                .addInput(Ingredient.of(Items.NETHERITE_INGOT))
                .addInput(Ingredient.of(MRBlockInit.NOVICE_RITUAL_ANCHOR_BLOCK))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(Items.HEART_OF_THE_SEA))
                .addMana(Manas.FireMana, 1000)
                .addMana(Manas.AirMana, 1000)
                .addMana(Manas.EarthMana, 1000)
                .addMana(Manas.WaterMana, 1000)
                .addMana(Manas.OrderMana, 200)
                .addMana(Manas.SoulMana, 200)
                .addMana(Manas.EntropyMana, 100)
                .addMana(Manas.VoidMana, 100)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRBlockInit.SPELL_DESIGNER_BLOCK).setTier(Tier.NOVICE)
                .setCraftTime(100)
                .addInput(Ingredient.of(Items.GOLD_INGOT))
                .addInput(Ingredient.of(MRBlockInit.RUNE_CARVER_BLOCK))
                .addInput(Ingredient.of(Items.DIAMOND))
                .addInput(Ingredient.of(Blocks.ANVIL))
                .addMana(Manas.FireMana, 250)
                .addMana(Manas.AirMana, 250)
                .addMana(Manas.EarthMana, 250)
                .addMana(Manas.WaterMana, 250)
                .addMana(Manas.OrderMana, 50)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRBlockInit.RUNEWROUGHT_BENCH_BLOCK.get()).setTier(Tier.NOVICE)
                .setCraftTime(100)
                .addInput(Ingredient.of(ItemTags.WOOL))
                .addInput(Ingredient.of(Blocks.CRAFTING_TABLE))
                .addInput(Ingredient.of(Items.AMETHYST_SHARD))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addMana(Manas.EarthMana, 100)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRBlockInit.MANA_STORAGE_BLOCK).setTier(Tier.NOVICE)
                .setCraftTime(100)
                .addInput(Ingredient.of(Items.GOLD_BLOCK))
                .addInput(Ingredient.of(MRItemInit.RUNE_MATRIX_ITEM))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(Items.AMETHYST_BLOCK))
                .addMana(Manas.FireMana, 500)
                .addMana(Manas.AirMana, 500)
                .addMana(Manas.EarthMana, 500)
                .addMana(Manas.WaterMana, 500)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRItemInit.RUNE_MATRIX_ITEM)
                .setTier(Tier.NOVICE)
                .setCraftTime(10)
                .addInput(Ingredient.of(MRTagInit.SPELL_MANA_PROVIDER))
                .addInput(Ingredient.of(MRTagInit.SPELL_MANA_PROVIDER))
                .addInput(Ingredient.of(MRTagInit.MANA_INFUSED_BLOCK_ITEM))
                .addInput(Ingredient.of(MRTagInit.MANA_INFUSED_BLOCK_ITEM))
                .addMana(Manas.FireMana, 100)
                .addMana(Manas.AirMana, 100)
                .addMana(Manas.EarthMana, 100)
                .addMana(Manas.WaterMana, 100)
                .addMana(Manas.EntropyMana, 20)
                .addMana(Manas.OrderMana, 20)
                .addMana(Manas.SoulMana, 20)
                .addMana(Manas.VoidMana, 20)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRItemInit.RUNE_BRACELET_ITEM)
                .setTier(Tier.MASTER)
                .setCraftTime(45)
                .addInput(Ingredient.of(MRItemInit.RUNE_MATRIX_ITEM))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(Items.LEATHER, Items.RABBIT_HIDE))
                .addInput(Ingredient.of(Items.LEATHER, Items.RABBIT_HIDE))
                .addInput(Ingredient.of(Items.LEATHER, Items.RABBIT_HIDE))
                .addInput(Ingredient.of(Items.GOLD_INGOT))
                .addMana(Manas.EarthMana, 600)
                .addMana(Manas.OrderMana, 30)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(MRBlockInit.MANA_GENERATOR_BLOCK)
                .setTier(Tier.MASTER)
                .addInput(Ingredient.of(Blocks.FURNACE))
                .addInput(Ingredient.of(Items.NETHER_BRICK))
                .addInput(Ingredient.of(Items.NETHER_BRICK))
                .addInput(Ingredient.of(Items.AMETHYST_BLOCK))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(MRItemInit.RUNE_MATRIX_ITEM))
                .addMana(Manas.FireMana, 200)
                .addMana(Manas.OrderMana, 20)
                .save(output);


        new ManaConcentratorRecipeBuilder.Builder(MRItemInit.MARK_CONTAINER_ITEM).setTier(Tier.ASCENDED)
                .setCraftTime(100)
                .addInput(Ingredient.of(Items.BLACKSTONE))
                .addInput(Ingredient.of(Items.OBSIDIAN))
                .addInput(Ingredient.of(Items.NAME_TAG))
                .addInput(Ingredient.of(MRItemInit.RUNE_MATRIX_ITEM))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addInput(Ingredient.of(MRBlockInit.RUNE_BLOCK))
                .addInput(Ingredient.of(MRBlockInit.RUNE_BLOCK))
                .addInput(Ingredient.of(MRBlockInit.RUNE_BLOCK))
                .addInput(Ingredient.of(MRTagInit.MANA_INFUSED_BLOCK_ITEM))
                .addInput(Ingredient.of(Items.ECHO_SHARD))
                .addInput(Ingredient.of(Items.ECHO_SHARD))
                .addMana(Manas.SoulMana, 500)
                .addMana(Manas.EntropyMana, 150)
                .addMana(Manas.EarthMana, 500)
                .save(output);

    }

    private void makeManaInfusedRockRecipe(RecipeOutput output, DeferredBlock<Block> block, Ingredient ingredient, Mana mana) {
        new ManaConcentratorRecipeBuilder.Builder(block.asItem(), 4).setTier(Tier.NOVICE)
                .setCraftTime(100)
                .addInput(ingredient)
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addMana(mana, 15)
                .save(output);
    }
}
