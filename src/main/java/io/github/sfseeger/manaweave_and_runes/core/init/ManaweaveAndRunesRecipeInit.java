package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.recipes.mana_generator.ManaGeneratorRecipe;
import io.github.sfseeger.lib.common.recipes.mana_generator.ManaGeneratorRecipeSerializer;
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

    public static final Supplier<RecipeType<ManaGeneratorRecipe>> MANA_GENERATOR_RECIPE =
            RECIPE_TYPES.register(
                    "mana_generator_recipe",
                    // We need the qualifying generic here due to generics being generics.
                    () -> RecipeType.<ManaGeneratorRecipe>simple(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana_generator_recipe"))
            );

    public static final Supplier<RecipeSerializer<ManaGeneratorRecipe>> MANA_GENERATOR_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register(
                    "mana_generator_recipe",
                    ManaGeneratorRecipeSerializer::new
            );
}
