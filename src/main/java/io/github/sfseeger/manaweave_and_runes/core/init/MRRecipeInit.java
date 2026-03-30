package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipe;
import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipeSerializer;
import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipe;
import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipeSerializer;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MRRecipeInit {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ManaweaveAndRunes.MODID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ManaweaveAndRunes.MODID);


    public static final Supplier<RecipeType<RuneCarverRecipe>> RUNE_CARVER_RECIPE_TYPE = RECIPE_TYPES.register(
            "rune_carver",
            () -> RecipeType.<RuneCarverRecipe>simple(
                    ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "rune_carver")));

    public static final Supplier<RecipeSerializer<RuneCarverRecipe>> RUNE_CARVER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register(
                    "rune_carver",
                    RuneCarverRecipeSerializer::new);


    public static final Supplier<RecipeType<ManaConcentratorRecipe>> MANA_CONCENTRATOR_RECIPE_TYPE =
            RECIPE_TYPES.register(
                    "mana_concentrator",
                    () -> RecipeType.<ManaConcentratorRecipe>simple(
                            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana_concentrator")));

    public static final Supplier<RecipeSerializer<ManaConcentratorRecipe>> MANA_CONCENTRATOR_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register(
                    "mana_concentrator",
                    ManaConcentratorRecipeSerializer::new);

    public static <C extends RecipeInput, T extends Recipe<C>> List<T> getRecipes(Level world, RecipeType<T> type) {
        return world.getRecipeManager()
                .getAllRecipesFor(type)
                .stream()
                .map(RecipeHolder::value)
                .collect(Collectors.toList());
    }
}
