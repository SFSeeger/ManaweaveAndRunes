package io.github.sfseeger.lib.client.patchouli;

import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class RuneCarvingComponentProcessor implements IComponentProcessor {
    private RuneCarverRecipe recipe;
    private @Nullable RuneCarverRecipe recipe2;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        RecipeManager manager = level.getRecipeManager();
        RegistryAccess access = level.registryAccess();
        recipe = getRecipe(manager, variables.get("recipe", access).asString());
        if (recipe == null) throw new IllegalArgumentException("Recipe 1 is required");
        recipe2 = variables.has("recipe2") ? getRecipe(manager, variables.get("recipe2", access).asString()) : null;

    }

    @Override
    public IVariable process(Level level, String key) {
        RegistryAccess access = level.registryAccess();


        if (key.startsWith("chisel")) {
            int index = Integer.parseInt(key.substring("chisel".length())) - 1;
            ItemStack[] stacks;
            switch (index) {
                case 0 -> stacks = recipe.getChisel().getItems();
                case 1 -> stacks = recipe2 == null ? new ItemStack[0] : recipe2.getChisel().getItems();
                default -> stacks = new ItemStack[0];
            }

            return IVariable.from(stacks.length == 0 ? ItemStack.EMPTY : stacks[0], level.registryAccess());
        }
        if (key.startsWith("template")) {
            int index = Integer.parseInt(key.substring("template".length())) - 1;
            ItemStack[] stacks;
            switch (index) {
                case 0 -> stacks = recipe.getRuneTemplate().getItems();
                case 1 -> stacks = recipe2 == null ? new ItemStack[0] : recipe2.getRuneTemplate().getItems();
                default -> stacks = new ItemStack[0];
            }
            return IVariable.from(stacks.length == 0 ? ItemStack.EMPTY : stacks[0], level.registryAccess());
        }
        if (key.startsWith("ingredient")) {
            int index = Integer.parseInt(key.substring("ingredient".length())) - 1;

            ItemStack[] stacks;
            switch (index) {
                case 0 -> stacks = recipe.getRuneBase().getItems();
                case 1 -> stacks = recipe2 == null ? new ItemStack[0] : recipe2.getRuneBase().getItems();
                default -> stacks = new ItemStack[0];
            }
            return IVariable.from(stacks.length == 0 ? ItemStack.EMPTY : stacks[0], level.registryAccess());
        }
        if (key.startsWith("output")) {
            int index = Integer.parseInt(key.substring("output".length())) - 1;
            ItemStack output;
            switch (index) {
                case 0 -> output = recipe.getResultItem(access);
                case 1 -> output = recipe2 == null ? ItemStack.EMPTY : recipe2.getResultItem(access);
                default -> output = ItemStack.EMPTY;
            }

            return IVariable.from(output, level.registryAccess());
        }

        return null;
    }

    private RuneCarverRecipe getRecipe(RecipeManager manager, String recipeId) {
        if (recipeId == null || recipeId.isEmpty()) {
            return null;
        }

        Recipe<?> anyRecipe =
                manager.byKey(ResourceLocation.tryParse(recipeId)).orElseThrow(IllegalArgumentException::new).value();
        if (anyRecipe instanceof RuneCarverRecipe r) {
            return r;
        } else {
            throw new IllegalArgumentException("Cannot find Rune Carver Recipe");
        }
    }
}
