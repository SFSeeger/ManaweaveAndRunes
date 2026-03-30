package io.github.sfseeger.manaweave_and_runes.compat.jei.categories;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipe;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ListIterator;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ManaConcentrationRecipeCategory extends AbstractMRRecipeCategory<ManaConcentratorRecipe> {
    public static final RecipeType<ManaConcentratorRecipe> TYPE = RecipeType.create("manaweave_and_runes",
                                                                                    "mana_concentration",
                                                                                    ManaConcentratorRecipe.class);

    private static final int[] squareSizes = {20, 40, 60};
    private IDrawableAnimated recipeArrow;

    public ManaConcentrationRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper,
              new Info("jei.manaweave_and_runes.mana_concentrator", MRBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK, 170,
                       170));
    }

    @Override
    public RecipeType<ManaConcentratorRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public void draw(ManaConcentratorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int pedestalsToRender = recipe.tier().ordinal() * 4;
        int x = getWidth() / 2 - 8;
        int y = getHeight() / 2 - 8;

        int i = 0;
        /*
        while (i < pedestalsToRender) {
            int squareIndex = i / 4;
            int cornerIndex = i % 4;
            int offsetX =
                    (cornerIndex == 0 || cornerIndex == 2) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
            int offsetY =
                    (cornerIndex == 0 || cornerIndex == 1) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
            addPedestal(guiGraphics, x + offsetX + 4, y + offsetY);
            i++;
        }*/

        for (Map.Entry<Mana, Integer> entry : recipe.manaMap().entrySet()) {
            addMana(guiGraphics, i % 4 * 43, getHeight() - 30 + i / 4 * 15, mouseX, mouseY,
                    entry.getKey(), entry.getValue());
            i++;
        }
        recipeArrow.draw(guiGraphics, x + 20, y);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ManaConcentratorRecipe recipe, IFocusGroup focuses) {
        ListIterator<Ingredient> iterator = recipe.getIngredients().listIterator();

        int x = getWidth() / 2 - 8;
        int y = getHeight() / 2 - 8;

        while (iterator.hasNext()) {
            int i = iterator.nextIndex();
            int squareIndex = i / 4;
            int cornerIndex = i % 4;
            int offsetX =
                    (cornerIndex == 0 || cornerIndex == 2) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
            int offsetY =
                    (cornerIndex == 0 || cornerIndex == 1) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
            builder.addInputSlot(x + offsetX, y + offsetY).addIngredients(iterator.next());
        }

        ItemLike catalyst = MRBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK;
        switch (recipe.tier()) {
            case MASTER -> catalyst = MRBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK;
            case ASCENDED -> catalyst = MRBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK;
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, x, y)
                .addItemLike(catalyst);

        builder.addOutputSlot(x + 45, y).addItemStack(recipe.result());

        recipeArrow = guiHelper.createAnimatedRecipeArrow(recipe.craftTime());
    }
}
