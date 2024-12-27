package io.github.sfseeger.lib.common.recipes.mana_concentrator;

import com.mojang.datafixers.util.Pair;
import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesRecipeInit;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ManaConcentratorRecipe(Tier tier, List<Ingredient> inputs, Map<Mana, Integer> manaMap, int craftTime,
                                     ItemStack result) implements Recipe<ManaConcentratorInput> {
    public ManaConcentratorRecipe(Tier tier, List<Ingredient> inputs, Map<Mana, Integer> manaMap, int craftTime,
            ItemStack result) {
        this.tier = tier;
        this.inputs = inputs;
        this.manaMap = manaMap;
        this.craftTime = craftTime;
        this.result = result;
    }

    public ManaConcentratorRecipe(Tier tier, List<Ingredient> inputs, List<Pair<Holder<Mana>, Integer>> manaList,
            int craftTime,
            ItemStack result) {
        this(tier, inputs,
             manaList.stream().collect(Collectors.toMap(pair -> (pair.getFirst().value()), Pair::getSecond)), craftTime,
             result);
    }

    @Override
    public boolean matches(ManaConcentratorInput manaConcentratorInput, Level level) {
        return manaConcentratorInput.matches(tier, inputs);
    }

    @Override
    public ItemStack assemble(ManaConcentratorInput manaConcentratorInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.inputs.size();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ManaweaveAndRunesRecipeInit.MANA_CONCENTRATOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ManaweaveAndRunesRecipeInit.MANA_CONCENTRATOR_RECIPE_TYPE.get();
    }

    public List<Pair<Holder<Mana>, Integer>> manaMapAsList() {
        return this.manaMap.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey().registryHolder(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
