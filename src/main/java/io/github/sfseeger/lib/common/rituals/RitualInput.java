package io.github.sfseeger.lib.common.rituals;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public class RitualInput {
    public static final Codec<RitualInput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.listOf(1, 64).fieldOf("initialItemCost").forGetter(RitualInput::getInitialItemCost),
            Ingredient.CODEC.listOf(1, 64).fieldOf("tickItemCost").forGetter(RitualInput::getTickItemCost),
            Mana.MANAS_WITH_AMOUNT_CODEC.fieldOf("manaCost").forGetter(RitualInput::getManaCostAsList)
    ).apply(instance, RitualInput::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RitualInput> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), RitualInput::getInitialItemCost,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), RitualInput::getTickItemCost,
            Mana.MANA_MAP_STREAM_CODEC, RitualInput::getManaCost,
            RitualInput::new
    );
    private final List<Ingredient> initialItemCost;
    private final List<Ingredient> tickItemCost;
    private final Map<Mana, Integer> manaCost;

    public RitualInput(List<Ingredient> initialItemCost, List<Ingredient> tickItemCost,
            List<Pair<Holder<Mana>, Integer>> manaList) {
        this(initialItemCost, tickItemCost, Mana.manaMapFromList(manaList));
    }

    public RitualInput(List<Ingredient> initialItemCost, List<Ingredient> tickItemCost, Map<Mana, Integer> manaCost) {
        this.initialItemCost = initialItemCost;
        this.tickItemCost = tickItemCost;
        this.manaCost = manaCost;
    }

    public List<Ingredient> getInitialItemCost() {
        return initialItemCost;
    }

    public List<Ingredient> getTickItemCost() {
        return tickItemCost;
    }

    public Map<Mana, Integer> getManaCost() {
        return manaCost;
    }

    public List<Pair<Holder<Mana>, Integer>> getManaCostAsList() {
        return Mana.manaMapAsList(manaCost);
    }

    public boolean matches(List<Ingredient> items) {
        return initialItemCost.stream().allMatch(ingredient -> items.stream().anyMatch(ingredient::equals));
    }
}
