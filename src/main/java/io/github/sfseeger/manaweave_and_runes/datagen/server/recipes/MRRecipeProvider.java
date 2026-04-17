package io.github.sfseeger.manaweave_and_runes.datagen.server.recipes;

import io.github.sfseeger.lib.common.items.RuneCarvingTemplate;
import io.github.sfseeger.lib.datagen.recipes.RuneCarverRecipeBuilder;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class MRRecipeProvider extends RecipeProvider {
    private final CompletableFuture<HolderLookup.Provider> registries;

    public MRRecipeProvider(PackOutput output,
                            CompletableFuture<HolderLookup.Provider> registries
    ) {
        super(output, registries);
        this.registries = registries;
    }

    private ItemStack itemStackFromRegistry(Supplier<? extends Item> itemSupplier) {
        return new ItemStack(itemSupplier.get());
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        generateRuneCarverRecipes(recipeOutput);
        generateCraftingRecipes(recipeOutput);
        new ManaConcentratorRecipeProvider().register(this, recipeOutput);
        new SpellRecipeProvider().register(this, recipeOutput);
    }

    private void generateRuneCarverRecipes(RecipeOutput recipeOutput) {
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_FIRE_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_fire_rune_template", has(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_AIR_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.AIR_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_air_rune_template", has(MRItemInit.AIR_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);
        new RuneCarverRecipeBuilder(itemStackFromRegistry(MRItemInit.AMETHYST_EARTH_RUNE_ITEM),
                                    Ingredient.of(MRItemInit.DIAMOND_CHISEL.asItem()),
                                    Ingredient.of(MRItemInit.AMETHYST_BASE_RUNE.asItem()),
                                    Ingredient.of(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE))
                .unlockedBy("has_earth_template", has(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE.asItem()))
                .save(recipeOutput);
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
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MRItemInit.MANA_WEAVER_WAND_ITEM)
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

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MRBlockInit.SCRYING_POOL_BLOCK)
                .pattern("B B")
                .pattern("BAB")
                .define('B', Blocks.BLACKSTONE)
                .define('A', Blocks.AMETHYST_BLOCK)
                .unlockedBy("has_blackstone", has(Blocks.BLACKSTONE))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, new ItemStack(MRItemInit.BLANK_SPELL_PART.get(), 4))
                .pattern("RRR")
                .pattern("RAR")
                .pattern("RRR")
                .define('R', MRBlockInit.RUNE_BLOCK)
                .define('A', Items.AMETHYST_SHARD)
                .unlockedBy("has_rune_block", has(MRBlockInit.RUNE_BLOCK.asItem()))
                .save(recipeOutput);

        makeTemplateRecipe(recipeOutput,
                           MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE,
                           Ingredient.of(Items.PAPER),
                           Ingredient.of(Tags.Items.STONES));
        makeTemplateRecipe(recipeOutput,
                           MRItemInit.AIR_RUNE_CARVING_TEMPLATE,
                           Ingredient.of(Items.DIAMOND),
                           Ingredient.of(Items.FEATHER),
                           Ingredient.of(Tags.Items.STONES));
        makeTemplateRecipe(recipeOutput,
                           MRItemInit.FIRE_RUNE_CARVING_TEMPLATE,
                           Ingredient.of(Items.DIAMOND),
                           Ingredient.of(Items.COAL, Items.CHARCOAL),
                           Ingredient.of(Tags.Items.STONES));
        makeTemplateRecipe(recipeOutput,
                           MRItemInit.WATER_RUNE_CARVING_TEMPLATE,
                           Ingredient.of(Items.DIAMOND),
                           Ingredient.of(Items.SAND),
                           Ingredient.of(Tags.Items.STONES));
        makeTemplateRecipe(recipeOutput,
                           MRItemInit.EARTH_RUNE_CARVING_TEMPLATE,
                           Ingredient.of(Items.DIAMOND),
                           Ingredient.of(Items.DIRT, Items.COBBLESTONE),
                           Ingredient.of(Tags.Items.STONES));
        makeTemplateRecipe(recipeOutput,
                           MRItemInit.ENTROPY_RUNE_CARVING_TEMPLATE,
                           Ingredient.of(MRItemInit.TANZANITE),
                           Ingredient.of(Items.TNT),
                           Ingredient.of(Tags.Items.STONES));
        makeTemplateRecipe(recipeOutput,
                           MRItemInit.ORDER_RUNE_CARVING_TEMPLATE,
                           Ingredient.of(MRItemInit.TANZANITE),
                           Ingredient.of(Items.REDSTONE),
                           Ingredient.of(Blocks.TUFF));
        makeTemplateRecipe(recipeOutput,
                           MRItemInit.SOUL_RUNE_CARVING_TEMPLATE,
                           Ingredient.of(MRItemInit.TANZANITE),
                           Ingredient.of(Tags.Items.FOODS_RAW_MEAT),
                           Ingredient.of(Blocks.SOUL_SAND));
        makeTemplateRecipe(recipeOutput,
                           MRItemInit.VOID_RUNE_CARVING_TEMPLATE,
                           Ingredient.of(MRItemInit.TANZANITE),
                           Ingredient.of(Items.BUCKET),
                           Ingredient.of(Tags.Items.STONES));


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MRBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK)
                .pattern("GGG")
                .pattern("A#A")
                .pattern("PPP")
                .define('A', Items.AMETHYST_BLOCK)
                .define('G', Items.GLASS)
                .define('#', Items.IRON_BLOCK)
                .define('P', Ingredient.of(Tags.Items.STRIPPED_WOODS))
                .unlockedBy("has_amethyst", has(Items.AMETHYST_BLOCK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MRBlockInit.MANA_TRANSMITTER_BLOCK, 2)
                .pattern("G")
                .pattern("A")
                .pattern("G")
                .define('G', Items.GOLD_INGOT)
                .define('A', Items.AMETHYST_BLOCK)
                .unlockedBy("has_gold", has(Items.GOLD_INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MRBlockInit.RUNE_PEDESTAL_BLOCK, 2)
                .pattern("RRR")
                .pattern(" R ")
                .pattern("RRR")
                .define('R', MRBlockInit.RUNE_BLOCK)
                .unlockedBy("has_rune_block", has(MRBlockInit.RUNE_BLOCK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MRBlockInit.RUNE_CARVER_BLOCK)
                .pattern("II")
                .pattern("XX")
                .pattern("XX")
                .define('I', Items.IRON_INGOT)
                .define('X', Blocks.STONE_BRICKS)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, MRBlockInit.TANZANITE_BLOCK)
                .pattern("TT")
                .pattern("TT")
                .define('T', MRItemInit.TANZANITE)
                .unlockedBy("has_tanzanite", has(MRItemInit.TANZANITE))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MRBlockInit.NOVICE_MANA_COLLECTOR)
                .pattern("GGG")
                .pattern("W W")
                .pattern("SAS")
                .define('G', Items.GOLD_INGOT)
                .define('W', ItemTags.PLANKS)
                .define('S', Items.STONE_BRICKS)
                .define('A', Items.AMETHYST_BLOCK)
                .unlockedBy("has_gold", has(Items.GOLD_INGOT))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PatchouliAPI.get()
                        .getBookStack(ManaweaveAndRunes.asResource("manaweavers_guide")))
                .requires(Items.BOOK)
                .requires(Items.AMETHYST_SHARD)
                .unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD))
                .save(recipeOutput);

    }

    private void makeTemplateRecipe(RecipeOutput recipeOutput, DeferredItem<RuneCarvingTemplate> template,
                                    Ingredient specificIngredient, Ingredient runeIngredient
    ) {
        makeTemplateRecipe(recipeOutput, template, Ingredient.of(Items.AMETHYST_SHARD), specificIngredient,
                           runeIngredient);
    }

    private void makeTemplateRecipe(RecipeOutput recipeOutput, DeferredItem<RuneCarvingTemplate> template,
                                    Ingredient channelIngredient,
                                    Ingredient specificIngredient, Ingredient runeIngredient
    ) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, template.get(), 2)
                .pattern("STS")
                .pattern("SAS")
                .pattern("SPS")
                .define('T', template)
                .define('S', runeIngredient)
                .define('A', channelIngredient)
                .define('P', specificIngredient)
                .unlockedBy("has_rune_block_template", has(template.get()))
                .save(recipeOutput);
    }
}
