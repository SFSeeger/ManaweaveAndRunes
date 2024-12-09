package io.github.sfseeger.lib.common.recipes.mana_generator;

import io.github.sfseeger.lib.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesRecipeInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class ManaGeneratorRecipe implements Recipe<ManaGeneratorRecipeInput> {
    protected final CookingBookCategory category;
    protected final String group;
    private final Ingredient inputItem;
    private final Mana outputMana;
    private final int manaAmmount;
    private final int burnTime;
    private final float experience;

    public ManaGeneratorRecipe(CookingBookCategory category, String group, Ingredient inputItem, Mana outputMana, int manaAmmount, int burnTime, float experience) {
        this.category = category;
        this.group = group;
        this.inputItem = inputItem;
        this.outputMana = outputMana;
        this.manaAmmount = manaAmmount;
        this.burnTime = burnTime;
        this.experience = experience;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(inputItem);
        return ingredients;
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY; // TODO: Maybe show an icon for the mana or the rune symbol?
    }

    @Override
    public boolean matches(ManaGeneratorRecipeInput input, Level level) {
        return this.inputItem.test(input.stack());
    }

    @Override
    public ItemStack assemble(ManaGeneratorRecipeInput input, HolderLookup.Provider provider) {
        // Since no actual item gets created, we return an empty stack
        return ItemStack.EMPTY;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public RecipeType<?> getType() {
        return ManaweaveAndRunesRecipeInit.MANA_GENERATOR_RECIPE.get();
    }

    public CookingBookCategory getCategory() {
        return category;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public float getExperience() {
        return experience;
    }

    public Mana getOutputMana() {
        return outputMana;
    }
    public int getManaAmmount(){
        return manaAmmount;
    }
}
