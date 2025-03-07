package io.github.sfseeger.lib.common.recipes.rune_carver;

import io.github.sfseeger.manaweave_and_runes.core.init.MRRecipeInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RuneCarverRecipe implements Recipe<RuneCarverRecipeInput> {
    private final Ingredient chisel;
    private final Ingredient runeBase;
    private final Ingredient runeTemplate;
    private final ItemStack result;

    public RuneCarverRecipe(Ingredient chisel, Ingredient runeBase, Ingredient runeTemplate, ItemStack result) {
        this.chisel = chisel;
        this.runeBase = runeBase;
        this.runeTemplate = runeTemplate;
        this.result = result;
    }

    @Override
    public boolean matches(RuneCarverRecipeInput runeCarverRecipeInput, Level level) {
        return this.chisel.test(runeCarverRecipeInput.chisel())
                && this.runeBase.test(runeCarverRecipeInput.runeBasePlate())
                && this.runeTemplate.test(runeCarverRecipeInput.runeTemplate());
    }

    @Override
    public ItemStack assemble(RuneCarverRecipeInput runeCarverRecipeInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.chisel);
        list.add(this.runeBase);
        list.add(this.runeTemplate);
        return list;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MRRecipeInit.RUNE_CARVER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MRRecipeInit.RUNE_CARVER_RECIPE_TYPE.get();
    }

    public Ingredient getChisel() {
        return this.chisel;
    }

    public Ingredient getRuneBase() {
        return this.runeBase;
    }

    public Ingredient getRuneTemplate(){return this.runeTemplate;}

    public ItemStack getResult() {
        return this.result;
    }

    public boolean isChiselIngredient(ItemStack itemStack) {
        return this.chisel.test(itemStack);
    }

    public boolean isTemplateIngredient(ItemStack itemStack) {
        return this.runeTemplate.test(itemStack);
    }

    public boolean isBaseIngredient(ItemStack itemStack) {
        return this.runeBase.test(itemStack);
    }
}
