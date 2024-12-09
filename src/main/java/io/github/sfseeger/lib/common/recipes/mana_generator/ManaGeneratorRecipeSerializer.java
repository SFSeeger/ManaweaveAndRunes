package io.github.sfseeger.lib.common.recipes.mana_generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.mana.Mana;
import io.github.sfseeger.lib.mana.ManaRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ManaGeneratorRecipeSerializer implements RecipeSerializer<ManaGeneratorRecipe> {
    public static final MapCodec<ManaGeneratorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ManaGeneratorRecipe::getInputItem),
            Mana.CODEC.fieldOf("output_mana").forGetter(ManaGeneratorRecipe::getOutputMana),
            Codec.INT.fieldOf("mana_amount").forGetter(ManaGeneratorRecipe::getManaAmount),
            Codec.INT.fieldOf("burn_time").forGetter(ManaGeneratorRecipe::getBurnTime)
            ).apply(inst, ManaGeneratorRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ManaGeneratorRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, ManaGeneratorRecipe::getInputItem,
                    ByteBufCodecs.registry(ManaRegistry.MANA_REGISTRY_KEY), ManaGeneratorRecipe::getOutputMana,
                    ByteBufCodecs.INT, ManaGeneratorRecipe::getManaAmount,
                    ByteBufCodecs.INT, ManaGeneratorRecipe::getBurnTime,
                    ManaGeneratorRecipe::new
            );


    @Override
    public MapCodec<ManaGeneratorRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ManaGeneratorRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
