package io.github.sfseeger.manaweave_and_runes.compat.jei.categories;

import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipe;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.compat.jei.ManaweaveAndRunesJEIPlugin;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RuneCarverRecipeCategory implements IRecipeCategory<RuneCarverRecipe> {
    public static final RecipeType<RuneCarverRecipe> TYPE =
            RecipeType.create(ManaweaveAndRunes.MODID, "rune_carver", RuneCarverRecipe.class);
    private final Component localizedName;
    private final IDrawable icon;
    private final IGuiHelper guiHelper;

    public RuneCarverRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        localizedName = Component.translatable("jei.manaweave_and_runes.rune_carver");
        ItemStack renderStack = new ItemStack(MRBlockInit.RUNE_CARVER_BLOCK);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, renderStack.copy());
    }

    @Override
    public RecipeType<RuneCarverRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public int getWidth() {
        return 120;
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(RuneCarverRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiHelper.getRecipeArrow().draw(guiGraphics, 70, 11);
        ManaweaveAndRunesJEIPlugin.slotDrawable.draw(guiGraphics, 9, 11);
        ManaweaveAndRunesJEIPlugin.slotDrawable.draw(guiGraphics, 29, 11);
        ManaweaveAndRunesJEIPlugin.slotDrawable.draw(guiGraphics, 49, 11);
        ManaweaveAndRunesJEIPlugin.slotDrawable.draw(guiGraphics, 99, 11);

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RuneCarverRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(10, 12).addIngredients(recipe.chisel());
        builder.addInputSlot(30, 12).addIngredients(recipe.runeTemplate());
        builder.addInputSlot(50, 12).addIngredients(recipe.runeBase());

        builder.addOutputSlot(100, 12).addItemStack(recipe.result());

    }
}
