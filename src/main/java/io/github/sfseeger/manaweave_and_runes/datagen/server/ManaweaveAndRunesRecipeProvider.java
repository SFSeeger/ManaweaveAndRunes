package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.datagen.recipes.ManaConcentratorRecipeBuilder;
import io.github.sfseeger.lib.datagen.recipes.RuneCarverRecipeBuilder;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaInit;
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

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ManaweaveAndRunesRecipeProvider extends RecipeProvider {
    private final CompletableFuture<HolderLookup.Provider> registries;
    public ManaweaveAndRunesRecipeProvider(PackOutput output,
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
                                    Ingredient.of(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .unlockedBy("has_base_rune", has(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM),
                                    Ingredient.of(ManaweaveAndRunesItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .unlockedBy("has_base_rune", has(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.asItem()))
                .save(recipeOutput);


        new ManaConcentratorRecipeBuilder.Builder()
                .setTier(Tier.NOVICE)
                .addInput(Ingredient.of(Items.DIAMOND))
                .addInput(Ingredient.of(Items.NETHER_WART))
                .addMana(ManaInit.FIRE_MANA.get(), 10)
                .setCraftTime(100)
                .unlockedBy("has_nether_wart", has(Items.NETHER_WART))
                .setResult(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE))
                .save(recipeOutput);

        ItemStack netheritePickaxe = new ItemStack(Items.NETHERITE_PICKAXE);
        try {
            registries.get()
                    .lookup(Registries.ENCHANTMENT)
                    .get()
                    .get(Enchantments.EFFICIENCY)
                    .ifPresent(enchantment -> {
                        netheritePickaxe.enchant(enchantment, 3);
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new ManaConcentratorRecipeBuilder.Builder()
                .setTier(Tier.MASTER)
                .addInput(Ingredient.of(Items.REDSTONE))
                .addInput(Ingredient.of(Items.AMETHYST_SHARD))
                .addInput(Ingredient.of(Items.GOLD_BLOCK))
                .addInput(Ingredient.of(Items.NETHERITE_INGOT))
                .addInput(Ingredient.of(Items.NETHERITE_INGOT))
                .addMana(ManaInit.FIRE_MANA.get(), 400)
                .addMana(ManaInit.AIR_MANA.get(), 250)
                .setCraftTime(600)
                .setResult(netheritePickaxe)
                .save(recipeOutput);
    }
}
