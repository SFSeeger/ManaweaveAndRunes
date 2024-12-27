package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipe;
import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipeSerializer;
import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipe;
import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipeSerializer;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ManaweaveAndRunesRecipeInit {
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
}
