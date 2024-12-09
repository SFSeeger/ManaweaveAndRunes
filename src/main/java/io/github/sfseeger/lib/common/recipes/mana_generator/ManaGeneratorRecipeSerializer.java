package io.github.sfseeger.lib.common.recipes.mana_generator;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ManaGeneratorRecipeSerializer implements RecipeSerializer<ManaGeneratorRecipe> {
    public static final MapCodec<ManaGeneratorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ManaGeneratorRecipe::getInputItem),
            ).apply(inst, ManaGeneratorRecipe::new)
    );

    @Override
    public MapCodec<ManaGeneratorRecipe> codec() {
        return null;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ManaGeneratorRecipe> streamCodec() {
        return null;
    }
}
