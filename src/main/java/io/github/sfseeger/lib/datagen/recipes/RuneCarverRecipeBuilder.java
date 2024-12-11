package io.github.sfseeger.lib.datagen.recipes;

import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class RuneCarverRecipeBuilder implements RecipeBuilder {
    protected final ItemStack result;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    protected Ingredient chisel;
    protected Ingredient runeBase;

    public RuneCarverRecipeBuilder(ItemStack result, Ingredient chisel, Ingredient baseRune) {
        this.result = result;
        this.chisel = chisel;
        this.runeBase = baseRune;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation resourceLocation) {
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation))
                .rewards(AdvancementRewards.Builder.recipe(resourceLocation))
                .requirements(AdvancementRequirements.Strategy.AND);
        this.criteria.forEach(advancement::addCriterion);
        RuneCarverRecipe recipe = new RuneCarverRecipe(this.chisel, this.runeBase, this.result);
        output.accept(resourceLocation, recipe, advancement.build(resourceLocation.withPrefix("recipes/")));
    }
}
