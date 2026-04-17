package io.github.sfseeger.manaweave_and_runes.datagen.server.recipes;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.datagen.recipes.ManaConcentratorRecipeBuilder;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class SpellRecipeProvider implements IRecipeRegistrar {
    @Override
    public void register(RecipeProvider provider, RecipeOutput output) {
        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_type.self"))).setTier(Tier.NOVICE)
                .setCraftTime(250)
                .addInput(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addInput(Ingredient.of(Items.PAPER))
                .addInput(Ingredient.of(MRItemInit.BLANK_SPELL_PART))
                .addMana(Manas.SoulMana, 10)
                .addMana(Manas.OrderMana, 10)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_type.touch"))).setTier(Tier.NOVICE)
                .setCraftTime(250)
                .addInput(Ingredient.of(MRItemInit.POSITION_RUNE_ITEM))
                .addInput(Ingredient.of(Items.GOLD_INGOT))
                .addInput(Ingredient.of(MRItemInit.BLANK_SPELL_PART))
                .addMana(Manas.EarthMana, 100)
                .addMana(Manas.VoidMana, 10)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_type.projectile"))).setTier(Tier.NOVICE)
                .setCraftTime(250)
                .addInput(Ingredient.of(Items.ARROW))
                .addInput(Ingredient.of(Items.BOW))
                .addInput(Ingredient.of(MRItemInit.BLANK_SPELL_PART))
                .addMana(Manas.AirMana, 100)
                .addMana(Manas.EntropyMana, 10)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_type.rune"))).setTier(Tier.NOVICE)
                .setCraftTime(250)
                .addInput(Ingredient.of(MRItemInit.RUNE_MATRIX_ITEM))
                .addInput(Ingredient.of(Items.ENDER_EYE))
                .addInput(Ingredient.of(MRItemInit.BLANK_SPELL_PART))
                .addMana(Manas.VoidMana, 25)
                .addMana(Manas.EntropyMana, 30)
                .addMana(Manas.OrderMana, 25)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_effect.burn"))).setTier(Tier.NOVICE)
                .setCraftTime(250)
                .addInput(Ingredient.of(Items.FLINT_AND_STEEL))
                .addInput(Ingredient.of(Items.FIRE_CHARGE))
                .addInput(Ingredient.of(MRItemInit.BLANK_SPELL_PART))
                .addMana(Manas.FireMana, 300)
                .addMana(Manas.OrderMana, 300)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_effect.heal"))).setTier(Tier.MASTER)
                .setCraftTime(300)
                .addInput(Ingredient.of(Items.GOLDEN_APPLE))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(MRItemInit.BLANK_SPELL_PART))
                .addMana(Manas.SoulMana, 40)
                .addMana(Manas.OrderMana, 40)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_effect.harm"))).setTier(Tier.MASTER)
                .setCraftTime(300)
                .addInput(Ingredient.of(Items.DIAMOND_SWORD))
                .addInput(Ingredient.of(Items.REDSTONE))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(MRItemInit.BLANK_SPELL_PART))
                .addMana(Manas.SoulMana, 20)
                .addMana(Manas.VoidMana, 20)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_effect.break"))).setTier(Tier.MASTER)
                .setCraftTime(300)
                .addInput(Ingredient.of(Items.IRON_AXE))
                .addInput(Ingredient.of(Items.IRON_SHOVEL))
                .addInput(Ingredient.of(Items.IRON_PICKAXE))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE))
                .addMana(Manas.EarthMana, 100)
                .addMana(Manas.VoidMana, 20)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_modifier.widen"))).setTier(Tier.MASTER)
                .setCraftTime(300)
                .addInput(Ingredient.of(Items.PISTON))
                .addInput(Ingredient.of(Items.PISTON))
                .addInput(Ingredient.of(Items.LEVER))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(MRItemInit.BLANK_SPELL_PART))
                .addMana(Manas.OrderMana, 50)
                .addMana(Manas.WaterMana, 300)
                .save(output);

        new ManaConcentratorRecipeBuilder.Builder(
                BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_modifier.elongate"))).setTier(
                        Tier.MASTER)
                .setCraftTime(300)
                .addInput(Ingredient.of(Items.PISTON))
                .addInput(Ingredient.of(Items.PISTON))
                .addInput(Ingredient.of(Items.STONE_BUTTON))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(MRItemInit.BLANK_SPELL_PART))
                .addMana(Manas.OrderMana, 50)
                .addMana(Manas.WaterMana, 300)
                .save(output);
    }
}
