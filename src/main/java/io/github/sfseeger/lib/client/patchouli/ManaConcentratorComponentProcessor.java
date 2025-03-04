package io.github.sfseeger.lib.client.patchouli;

import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class ManaConcentratorComponentProcessor implements IComponentProcessor {
    private ManaConcentratorRecipe recipe;


    @Override
    public void setup(Level level, IVariableProvider variables) {
        RecipeManager manager = level.getRecipeManager();
        RegistryAccess access = level.registryAccess();
        recipe = getRecipe(manager, variables.get("recipe", access).asString());
        if (recipe == null) throw new IllegalArgumentException("Recipe is required");
    }

    @Override
    public IVariable process(Level level, String key) {
        if (key.equals("output")) {
            return IVariable.from(recipe.result(), level.registryAccess());
        }

        return null;
    }

    private ManaConcentratorRecipe getRecipe(RecipeManager manager, String recipeId) {
        if (recipeId == null || recipeId.isEmpty()) {
            return null;
        }

        Recipe<?> anyRecipe =
                manager.byKey(ResourceLocation.tryParse(recipeId))
                        .orElseThrow(() -> new IllegalArgumentException("Cannot find recipe: " + recipeId))
                        .value();
        if (anyRecipe instanceof ManaConcentratorRecipe r) {
            return r;
        } else {
            throw new IllegalArgumentException("Cannot find Rune Carver Recipe");
        }
    }
}
