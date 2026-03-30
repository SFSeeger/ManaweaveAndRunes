package io.github.sfseeger.manaweave_and_runes.compat.jei;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.compat.jei.categories.ManaConcentrationRecipeCategory;
import io.github.sfseeger.manaweave_and_runes.compat.jei.categories.RuneCarverRecipeCategory;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRRecipeInit;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManaweaveAndRunesJEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = ManaweaveAndRunes.asResource("jei_plugin");
    private static final Comparator<Recipe<?>> BY_GROUP = Comparator.comparing(Recipe::getGroup);
    public static IDrawableStatic slotDrawable;

    private static <T extends Recipe<C>, C extends RecipeInput> List<T> sortRecipes(RecipeType<T> type, Comparator<? super T> comparator) {
        assert Minecraft.getInstance().level != null;
        List<T> recipes = MRRecipeInit.getRecipes(Minecraft.getInstance().level, type);
        recipes.sort(comparator);
        return recipes;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(
                new RuneCarverRecipeCategory(guiHelper)
        );
        registry.addRecipeCategories(
                new ManaConcentrationRecipeCategory(guiHelper)
        );

        slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(
                RuneCarverRecipeCategory.TYPE, sortRecipes(MRRecipeInit.RUNE_CARVER_RECIPE_TYPE.get(), BY_GROUP)
        );
        registry.addRecipes(
                ManaConcentrationRecipeCategory.TYPE,
                sortRecipes(MRRecipeInit.MANA_CONCENTRATOR_RECIPE_TYPE.get(), BY_GROUP)
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(
                new ItemStack(MRBlockInit.RUNE_CARVER_BLOCK),
                RuneCarverRecipeCategory.TYPE
        );
        registry.addRecipeCatalysts(
                ManaConcentrationRecipeCategory.TYPE,
                new ItemStack(MRBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK),
                new ItemStack(MRBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK),
                new ItemStack(MRBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK)
        );
    }
}