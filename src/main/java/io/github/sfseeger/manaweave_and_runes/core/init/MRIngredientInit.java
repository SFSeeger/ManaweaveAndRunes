package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.recipes.ingredients.PotionIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MRIngredientInit {
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, ManaweaveAndRunes.MODID);

    public static final Supplier<IngredientType<PotionIngredient>> POTION_INGREDIENT =
            INGREDIENT_TYPES.register("potion", () ->
                    new IngredientType<>(PotionIngredient.CODEC, PotionIngredient.STREAM_CODEC));
}
