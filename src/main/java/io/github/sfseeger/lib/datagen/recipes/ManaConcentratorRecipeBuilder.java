package io.github.sfseeger.lib.datagen.recipes;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;

public class ManaConcentratorRecipeBuilder extends SimpleRecipeBuilder {
    private final Tier tier;
    private final List<Ingredient> inputs;
    private final Map<Mana, Integer> manaMap;
    private final int craftTime;


    public ManaConcentratorRecipeBuilder(Tier tier, List<Ingredient> inputs, Map<Mana, Integer> manaMap, int craftTime,
            ItemStack result) {
        super(result);
        this.tier = tier;
        this.inputs = inputs;
        this.manaMap = manaMap;
        this.craftTime = craftTime;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation))
                .rewards(AdvancementRewards.Builder.recipe(resourceLocation))
                .requirements(AdvancementRequirements.Strategy.AND);
        this.criteria.forEach(advancement::addCriterion);
        ManaConcentratorRecipe recipe =
                new ManaConcentratorRecipe(this.tier, this.inputs, this.manaMap, this.craftTime, this.result);
        recipeOutput.accept(resourceLocation.withPrefix("mana_concentrator/"), recipe,
                            advancement.build(resourceLocation.withPrefix("recipes/")));
    }

    public static class Builder {
        protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
        private Tier tier;
        private List<Ingredient> inputs = new ArrayList<>();
        private Map<Mana, Integer> manaMap = new HashMap<>();
        private int craftTime;
        private ItemStack result;

        public Builder setTier(Tier tier) {
            this.tier = tier;
            return this;
        }

        public Builder setInputs(List<Ingredient> inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder addInput(Ingredient input) {
            this.inputs.add(input);
            return this;
        }

        public Builder setManaMap(Map<Mana, Integer> manaMap) {
            this.manaMap = manaMap;
            return this;
        }

        public Builder addMana(Mana mana, int amount) {
            this.manaMap.put(mana, amount);
            return this;
        }

        public Builder setCraftTime(int craftTime) {
            this.craftTime = craftTime;
            return this;
        }

        public Builder setResult(ItemStack result) {
            this.result = result;
            return this;
        }

        public Builder unlockedBy(String name, Criterion<?> criterion) {
            this.criteria.put(name, criterion);
            return this;
        }

        public ManaConcentratorRecipeBuilder build() {
            return new ManaConcentratorRecipeBuilder(tier, inputs, manaMap, craftTime, result);
        }

        public void save(RecipeOutput recipeOutput) {
            ManaConcentratorRecipeBuilder manaConcentratorRecipeBuilder = build();
            manaConcentratorRecipeBuilder.criteria.putAll(criteria);
            manaConcentratorRecipeBuilder.save(recipeOutput);
        }
    }
}
