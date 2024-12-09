package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.lib.datagen.ManaGeneratorRecipeBuilder;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ManaweaveAndRunesRecipeProvider extends RecipeProvider {

    public ManaweaveAndRunesRecipeProvider(PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        new ManaGeneratorRecipeBuilder(
                Ingredient.of(Items.COAL),
                ManaInit.FIRE_MANA.get(),
                30,
                200
        )
                .unlockedBy("has_item", has(ManaweaveAndRunesItemInit.FIRE_RUNE_ITEM.get()))
                .save(recipeOutput);
    }
}
