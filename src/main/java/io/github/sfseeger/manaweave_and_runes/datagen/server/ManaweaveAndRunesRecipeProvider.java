package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.lib.datagen.recipes.RuneCarverRecipeBuilder;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ManaweaveAndRunesRecipeProvider extends RecipeProvider {

    public ManaweaveAndRunesRecipeProvider(PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private ItemStack itemStackFromRegistry(Supplier<? extends Item> itemSupplier) {
        return new ItemStack(itemSupplier.get());
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        new RuneCarverRecipeBuilder(itemStackFromRegistry(ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM),
                                    Ingredient.of(ManaweaveAndRunesItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .unlockedBy("has_base_rune", has(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM),
                                    Ingredient.of(ManaweaveAndRunesItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .unlockedBy("has_base_rune", has(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .save(recipeOutput);
    }
}
