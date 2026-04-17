package io.github.sfseeger.manaweave_and_runes.datagen.server.recipes;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public interface IRecipeRegistrar {
    void register(RecipeProvider provider, RecipeOutput output);
}
