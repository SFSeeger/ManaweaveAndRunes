package io.github.sfseeger.lib.common.datamaps;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger;
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover;

import java.util.*;

public record ManaMapData(Map<Mana, Integer> manaMap) {
    public static final Codec<ManaMapData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Mana.MANAS_WITH_AMOUNT_CODEC.fieldOf("mana").forGetter(ManaMapData::manaMapAsList)
    ).apply(instance, ManaMapData::new));

    public static final AdvancedDataMapType<Item, ManaMapData, ManaMapRemover> MANA_MAP_DATA =
            AdvancedDataMapType.builder(
                            ResourceLocation.fromNamespaceAndPath("manaweave_and_runes", "mana_map_data"),
                            Registries.ITEM,
                            ManaMapData.CODEC
                    ).merger(new ManaMerger())
                    .remover(ManaMapRemover.CODEC)
                    .synced(ManaMapData.CODEC, true)
                    .build();

    public ManaMapData {
        if (manaMap == null) {
            manaMap = Map.of();
        }
    }

    public ManaMapData(List<Pair<Holder<Mana>, Integer>> list) {
        this(Mana.manaMapFromList(list));
    }

    public List<Pair<Holder<Mana>, Integer>> manaMapAsList() {
        return Mana.manaMapAsList(manaMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ManaMapData that)) return false;
        return Objects.equals(manaMap, that.manaMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(manaMap);
    }

    public static class ManaMerger implements DataMapValueMerger<Item, ManaMapData> {

        @Override
        public ManaMapData merge(Registry<Item> registry,
                Either<TagKey<Item>, ResourceKey<Item>> first, ManaMapData firstValue,
                Either<TagKey<Item>, ResourceKey<Item>> second, ManaMapData secondValue) {
            Map<Mana, Integer> mergedMap = new HashMap<>(firstValue.manaMap);
            secondValue.manaMap.forEach((key, value) -> mergedMap.merge(key, value, Integer::sum));
            return new ManaMapData(mergedMap);
        }
    }

    public record ManaMapRemover(Holder<Mana> key) implements DataMapValueRemover<Item, ManaMapData> {
        public static final Codec<ManaMapRemover> CODEC = Mana.CODEC.xmap(ManaMapRemover::new, ManaMapRemover::key);

        @Override
        public Optional<ManaMapData> remove(ManaMapData manaMapData, Registry<Item> registry,
                Either<TagKey<Item>, ResourceKey<Item>> either, Item item) {
            final Map<Mana, Integer> newMap = new HashMap<>(manaMapData.manaMap);
            newMap.remove(key.value());
            return newMap.isEmpty() ? Optional.empty() : Optional.of(new ManaMapData(newMap));
        }
    }
}
