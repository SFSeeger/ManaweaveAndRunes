package io.github.sfseeger.lib.common.recipes.mana_concentrator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.ManaRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.HashMap;

public class ManaConcentratorRecipeSerializer implements RecipeSerializer<ManaConcentratorRecipe> {
    public static final MapCodec<ManaConcentratorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                                                                                                     Tier.CODEC.fieldOf("tier").forGetter(ManaConcentratorRecipe::tier),
                                                                                                     Codec.list(Ingredient.CODEC).fieldOf("inputs").forGetter(ManaConcentratorRecipe::inputs),
                                                                                                     Mana.MANAS_WITH_AMOUNT_CODEC.fieldOf("manaMap").forGetter(ManaConcentratorRecipe::manaMapAsList),
                                                                                                     Codec.INT.fieldOf("craftTime").forGetter(ManaConcentratorRecipe::craftTime),
                                                                                                     ItemStack.CODEC.fieldOf("result").forGetter(ManaConcentratorRecipe::result)
                                                                                             ).apply(instance, ManaConcentratorRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ManaConcentratorRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Tier.STREAM_CODEC, ManaConcentratorRecipe::tier,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                    ManaConcentratorRecipe::getIngredients,
                    ByteBufCodecs.map(
                            HashMap::new,
                            ByteBufCodecs.registry(ManaRegistry.MANA_REGISTRY_KEY),
                            ByteBufCodecs.INT,
                            256
                    ), ManaConcentratorRecipe::manaMap,
                    ByteBufCodecs.INT, ManaConcentratorRecipe::craftTime,
                    ItemStack.STREAM_CODEC, ManaConcentratorRecipe::result,

                    ManaConcentratorRecipe::new
            );


    @Override
    public MapCodec<ManaConcentratorRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ManaConcentratorRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
