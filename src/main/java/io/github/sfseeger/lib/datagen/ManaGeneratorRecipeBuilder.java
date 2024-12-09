package io.github.sfseeger.lib.datagen;

import io.github.sfseeger.lib.common.recipes.mana_generator.ManaGeneratorRecipe;
import io.github.sfseeger.lib.mana.Mana;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ManaGeneratorRecipeBuilder implements RecipeBuilder {
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private final Ingredient inputItem;
    private final Mana outputMana;
    private final int manaAmount;
    private final int burnTime;
    @Nullable
    protected String group;


    public ManaGeneratorRecipeBuilder(Ingredient inputItem, Mana outputMana, int manaAmount, int burnTime) {
        this.inputItem = inputItem;
        this.outputMana = outputMana;
        this.manaAmount = manaAmount;
        this.burnTime = burnTime;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String s) {
        this.group = s;
        return this;
    }

    @Override
    public Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation))
                .rewards(AdvancementRewards.Builder.recipe(resourceLocation))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancement::addCriterion);
        ManaGeneratorRecipe recipe =
                new ManaGeneratorRecipe(this.inputItem, this.outputMana, this.manaAmount, this.burnTime);
        recipeOutput.accept(resourceLocation, recipe, advancement.build(resourceLocation.withPrefix("recipes/")));
    }
}
