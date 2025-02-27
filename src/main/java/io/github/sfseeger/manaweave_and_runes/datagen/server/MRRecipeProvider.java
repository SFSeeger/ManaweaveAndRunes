package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.datagen.recipes.ManaConcentratorRecipeBuilder;
import io.github.sfseeger.lib.datagen.recipes.RuneCarverRecipeBuilder;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import vazkii.patchouli.api.PatchouliAPI;

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
        generateRuneCarverRecipes(recipeOutput);
        generateManaConcentratorRecipes(recipeOutput);
        generateCraftingRecipes(recipeOutput);
    }

    private void generateManaConcentratorRecipes(RecipeOutput recipeOutput) {
        new ManaConcentratorRecipeBuilder.Builder()
                .setTier(Tier.NOVICE)
                .setCraftTime(100)
                .addInput(Ingredient.of(Items.FEATHER))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addMana(Manas.AirMana, 15)
                .setResult(new ItemStack(MRBlockInit.AIR_MANA_INFUSED_ROCK_BLOCK.asItem(), 4))
                .save(recipeOutput);

        new ManaConcentratorRecipeBuilder.Builder()
                .setTier(Tier.NOVICE)
                .setCraftTime(100)
                .addInput(Ingredient.of(Items.COAL, Items.CHARCOAL))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addMana(Manas.FireMana, 15)
                .setResult(new ItemStack(MRBlockInit.FIRE_MANA_INFUSED_ROCK_BLOCK.asItem(), 4))
                .save(recipeOutput);

        new ManaConcentratorRecipeBuilder.Builder()
                .setTier(Tier.NOVICE)
                .setCraftTime(100)
                .addInput(Ingredient.of(Items.DIRT, Items.COBBLESTONE))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                .addInput(Ingredient.of(Blocks.STONE_BRICKS))
                //.addMana(Manas.FireMana, 15)
                .setResult(new ItemStack(MRBlockInit.EARTH_MANA_INFUSED_ROCK_BLOCK.asItem(), 4))
                .save(recipeOutput);

        new ManaConcentratorRecipeBuilder.Builder()
                .setTier(Tier.NOVICE)
                .setCraftTime(500)
                .addInput(Ingredient.of(Items.GOLD_BLOCK))
                .addInput(Ingredient.of(MRItemInit.TANZANITE))
                .addInput(Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE))
                .addMana(Manas.FireMana, 80)
                .addMana(Manas.AirMana, 80)
                .setResult(new ItemStack(MRBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK))
                .save(recipeOutput);
    }

    private void generateRuneCarverRecipes(RecipeOutput recipeOutput) {
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_FIRE_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.AIR_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_fire_rune_template", has(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_AIR_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_air_rune_template", has(MRItemInit.AIR_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_EARTH_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_earth_template", has(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE.asItem()));
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_WATER_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.WATER_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_water_template", has(MRItemInit.WATER_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_VOID_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.VOID_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_void_template", has(MRItemInit.VOID_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_SOUL_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.SOUL_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_soul_template", has(MRItemInit.SOUL_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_ORDER_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.ORDER_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_order_template", has(MRItemInit.ORDER_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_ENTROPY_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.ENTROPY_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_entropy_template", has(MRItemInit.ENTROPY_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);


        new RuneCarverRecipeBuilder(new ItemStack(MRBlockInit.RUNE_BLOCK.asItem(), 4),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(Blocks.CHISELED_STONE_BRICKS),
                                    Ingredient.of(MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE))
                .unlockedBy("has_template", has(MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);

        new RuneCarverRecipeBuilder(new ItemStack(MRItemInit.POSITION_RUNE_ITEM.asItem()),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(Blocks.SMOOTH_STONE),
                                    Ingredient.of(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_position_rune_template", has(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);

        new RuneCarverRecipeBuilder(new ItemStack(MRItemInit.SOUL_CONTAINER_RUNE_ITEM.asItem()),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(Items.BONE),
                                    Ingredient.of(MRItemInit.SOUL_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_position_rune_template", has(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);

    }

    private void generateCraftingRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MRItemInit.DIAMOND_CHISEL)
                .pattern("  D")
                .pattern(" S ")
                .pattern("S  ")
                .define('D', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MRItemInit.MANA_CONNECTOR)
                .pattern(" SA")
                .pattern(" SS")
                .pattern("S  ")
                .define('S', Items.STICK)
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MRItemInit.MANA_WEAVER_WAND)
                .pattern("  A")
                .pattern(" S ")
                .pattern("S  ")
                .define('S', Items.STICK)
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MRItemInit.AMETHYST_BASE_RUNE)
                .pattern("AAA")
                .pattern("APA")
                .pattern("AAA")
                .define('A', Items.AMETHYST_SHARD)
                .define('P', Items.PAPER)
                .unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE, 2)
                .pattern("STS")
                .pattern("SAS")
                .pattern("SPS")
                .define('T', MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE)
                .define('S', Ingredient.of(Tags.Items.STONES))
                .define('A', Items.AMETHYST_SHARD)
                .define('P', Items.PAPER)
                .unlockedBy("has_rune_block_template", has(MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MRBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK)
                .pattern("AAA")
                .pattern("G#G")
                .pattern("PPP")
                .define('A', Items.AMETHYST_BLOCK)
                .define('G', Items.GLASS)
                .define('#', Items.IRON_BLOCK)
                .define('P', Ingredient.of(Tags.Items.STRIPPED_WOODS))
                .unlockedBy("has_amethyst", has(Items.AMETHYST_BLOCK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MRBlockInit.MANA_TRANSMITTER_BLOCK, 2)
                .pattern(" G ")
                .pattern(" A ")
                .pattern(" G ")
                .define('G', Items.GOLD_INGOT)
                .define('A', Items.AMETHYST_BLOCK)
                .unlockedBy("has_gold", has(Items.GOLD_INGOT))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PatchouliAPI.get()
                        .getBookStack(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "manaweavers_guide")))
                .requires(Items.BOOK)
                .requires(Items.AMETHYST_SHARD)
                .unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD))
                .save(recipeOutput);
    }
}
