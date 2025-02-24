package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.datagen.recipes.ManaConcentratorRecipeBuilder;
import io.github.sfseeger.lib.datagen.recipes.RuneCarverRecipeBuilder;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class MRRecipeProvider extends RecipeProvider {
    private final CompletableFuture<HolderLookup.Provider> registries;

    public MRRecipeProvider(PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
        this.registries = registries;
    }

    private ItemStack itemStackFromRegistry(Supplier<? extends Item> itemSupplier) {
        return new ItemStack(itemSupplier.get());
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        new RuneCarverRecipeBuilder(itemStackFromRegistry(ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM),
                                    Ingredient.of(ManaweaveAndRunesItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(ManaweaveAndRunesItemInit.AIR_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_base_rune", has(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM),
                                    Ingredient.of(ManaweaveAndRunesItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(ManaweaveAndRunesItemInit.FIRE_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_base_rune", has(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .save(recipeOutput);

        new RuneCarverRecipeBuilder(new ItemStack(ManaweaveAndRunesBlockInit.RUNE_BLOCK.asItem()),
                                    Ingredient.of(ManaweaveAndRunesItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(Blocks.CHISELED_STONE_BRICKS),
                                    Ingredient.of(ManaweaveAndRunesItemInit.RUNE_BLOCK_CARVING_TEMPLATE))
                .unlockedBy("has_template", has(ManaweaveAndRunesItemInit.RUNE_BLOCK_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);


        new ManaConcentratorRecipeBuilder.Builder()
                .setTier(Tier.NOVICE)
                .setCraftTime(100)
                .addInput(Ingredient.of(Items.FEATHER))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addMana(Manas.AirMana, 15)
                .setResult(new ItemStack(ManaweaveAndRunesBlockInit.MANA_INFUSED_ROCK_BLOCK.asItem(), 4))
                .save(recipeOutput);
    }
}
