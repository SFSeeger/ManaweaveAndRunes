package io.github.sfseeger.lib.common.recipes.mana_generator;

import io.github.sfseeger.lib.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesRecipeInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ManaGeneratorRecipe implements Recipe<ManaGeneratorRecipeInput> {
    //TODO: Add recipe book capability
    private final Ingredient inputItem;
    private final Mana outputMana;
    private final int manaAmount;
    private final int burnTime;

    public ManaGeneratorRecipe(Ingredient inputItem, Mana outputMana, int manaAmount, int burnTime) {
        this.inputItem = inputItem;
        this.outputMana = outputMana;
        this.manaAmount = manaAmount;
        this.burnTime = burnTime;
    }

    @Override
    public boolean matches(ManaGeneratorRecipeInput input, @NotNull Level level) {
        return this.inputItem.test(input.stack());
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ManaGeneratorRecipeInput input,
            HolderLookup.@NotNull Provider provider) {
        // Since no actual item gets created, we return an empty stack
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY; // TODO: Maybe show an icon for the mana or the rune symbol?
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(inputItem);
        return ingredients;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ManaweaveAndRunesRecipeInit.MANA_GENERATOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ManaweaveAndRunesRecipeInit.MANA_GENERATOR_RECIPE.get();
    }

    public int getBurnTime() {
        return burnTime;
    }

    public Mana getOutputMana() {
        return outputMana;
    }

    public Integer getManaAmount() {
        return manaAmount;
    }
}
