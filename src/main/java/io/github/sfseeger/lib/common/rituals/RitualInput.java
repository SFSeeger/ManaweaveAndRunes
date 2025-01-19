package io.github.sfseeger.lib.common.rituals;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.core.util.Utils;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RitualInput {
    public static final Codec<RitualInput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.listOf().fieldOf("initialItemCost").forGetter(RitualInput::getInitialItemCost),
            Ingredient.CODEC.listOf().fieldOf("tickItemCost").forGetter(RitualInput::getTickItemCost),
            Mana.MANAS_WITH_AMOUNT_CODEC.fieldOf("manaCost").forGetter(RitualInput::getManaCostAsList),
            Codec.INT.optionalFieldOf("itemRate", 1).forGetter(RitualInput::getItemRate),
            Codec.INT.optionalFieldOf("manaRate", 1).forGetter(RitualInput::getManaRate)
    ).apply(instance, RitualInput::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RitualInput> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), RitualInput::getInitialItemCost,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), RitualInput::getTickItemCost,
            Mana.MANA_MAP_STREAM_CODEC, RitualInput::getManaCost,
            ByteBufCodecs.INT, RitualInput::getItemRate,
            ByteBufCodecs.INT, RitualInput::getManaRate,
            RitualInput::new
    );
    private final List<Ingredient> initialItemCost;
    private final List<Ingredient> tickItemCost;
    private final Map<Mana, Integer> manaCost;
    private final int itemRate;
    private final int manaRate;

    public RitualInput(List<Ingredient> initialItemCost, List<Ingredient> tickItemCost,
            List<Pair<Holder<Mana>, Integer>> manaList, Integer itemRate, Integer manaRate) {
        this(initialItemCost, tickItemCost, Mana.manaMapFromList(manaList), itemRate, manaRate);
    }

    public RitualInput(List<Ingredient> initialItemCost, List<Ingredient> tickItemCost, Map<Mana, Integer> manaCost, Integer itemRate, Integer manaRate) {
        this.initialItemCost = initialItemCost;
        this.tickItemCost = tickItemCost;
        this.manaCost = manaCost;
        this.itemRate = itemRate;
        this.manaRate = manaRate;
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

    public boolean matches(List<ItemStack> items) {
        List<Ingredient> costs = new ArrayList<>(initialItemCost);
        costs.addAll(tickItemCost);
        return Utils.compareIngredientsToItems(costs, items);
    }

    public int getItemRate() {
        return itemRate;
    }

    public int getManaRate() {
        return manaRate;
    }

    public static class Builder {
        private List<Ingredient> initialItemCost = new ArrayList<>();
        private List<Ingredient> tickItemCost = new ArrayList<>();
        private Map<Mana, Integer> manaCost = new HashMap<>();
        private int itemRate = 1;
        private int manaRate = 1;

        public Builder setInitialItemCost(List<Ingredient> initialItemCost) {
            this.initialItemCost = initialItemCost;
            return this;
        }

        public Builder addInitialItemCost(Ingredient initialItemCost) {
            this.initialItemCost.add(initialItemCost);
            return this;
        }

        public Builder setTickItemCost(List<Ingredient> tickItemCost) {
            this.tickItemCost = tickItemCost;
            return this;
        }

        public Builder addTickItemCost(Ingredient tickItemCost) {
            this.tickItemCost.add(tickItemCost);
            return this;
        }

        public Builder setManaCost(Map<Mana, Integer> manaCost) {
            this.manaCost = manaCost;
            return this;
        }

        public Builder addManaCost(Mana mana, int amount) {
            this.manaCost.put(mana, amount);
            return this;
        }

        public Builder setItemRate(int itemRate) {
            this.itemRate = itemRate;
            return this;
        }
        public Builder setManaRate(int manaRate) {
            this.manaRate = manaRate;
            return this;
        }

        public RitualInput build() {
            return new RitualInput(initialItemCost, tickItemCost, manaCost, itemRate, manaRate);
        }
    }
}
