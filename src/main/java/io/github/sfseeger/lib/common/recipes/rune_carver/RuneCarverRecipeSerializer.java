package io.github.sfseeger.lib.common.recipes.rune_carver;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RuneCarverRecipeSerializer implements RecipeSerializer<RuneCarverRecipe> {
    public static final MapCodec<RuneCarverRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("chisel").forGetter(RuneCarverRecipe::getChisel),
            Ingredient.CODEC.fieldOf("rune_base").forGetter(RuneCarverRecipe::getRuneBase),
            Ingredient.CODEC.fieldOf("rune_template").forGetter(RuneCarverRecipe::getRuneTemplate),
            ItemStack.CODEC.fieldOf("result").forGetter(RuneCarverRecipe::getResult)
    ).apply(instance, RuneCarverRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RuneCarverRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, RuneCarverRecipe::getChisel,
                    Ingredient.CONTENTS_STREAM_CODEC, RuneCarverRecipe::getRuneBase,
                    Ingredient.CONTENTS_STREAM_CODEC, RuneCarverRecipe::getRuneTemplate,
                    ItemStack.STREAM_CODEC, RuneCarverRecipe::getResult,
                    RuneCarverRecipe::new
            );

    @Override
    public MapCodec<RuneCarverRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RuneCarverRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
