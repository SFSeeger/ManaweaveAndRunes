package io.github.sfseeger.lib.common.mana;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;
import java.util.stream.Collectors;

public class ManaDataComponent {
    public static final Codec<ManaDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Mana.MANAS_WITH_AMOUNT_CODEC.fieldOf("manaMap").forGetter(ManaDataComponent::toList)
    ).apply(instance, ManaDataComponent::fromList));

    public static final StreamCodec<RegistryFriendlyByteBuf, ManaDataComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.holderRegistry(ManaRegistry.MANA_REGISTRY_KEY),
                    ByteBufCodecs.INT
            ), ManaDataComponent::getManaMap,
            ManaDataComponent::new
    );

    private final Map<Holder<Mana>, Integer> manaMap;

    public ManaDataComponent() {
        this(new HashMap<>());
    }

    public ManaDataComponent(Map<Holder<Mana>, Integer> manaMap) {
        this.manaMap = manaMap;
    }

    public static ManaDataComponent fromList(List<Pair<Holder<Mana>, Integer>> list) {
        Map<Holder<Mana>, Integer> manaMap = list.stream().collect(
                Collectors.toMap(Pair::getFirst, Pair::getSecond)
        );
        return new ManaDataComponent(manaMap);
    }

    public static List<Pair<Holder<Mana>, Integer>> toList(ManaDataComponent manaDataComponent) {
        return manaDataComponent.manaMap.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public Map<Holder<Mana>, Integer> getManaMap() {
        return manaMap;
    }

    public Set<Holder<Mana>> getManaTypes() {
        return manaMap.keySet();
    }

    public int getManaAmount(Mana manaType) {
        Integer amount = manaMap.get(manaType.registryHolder());
        return amount != null ? amount : 0;
    }

    public int getManaAmount(Holder<Mana> manaType) {
        return manaMap.get(manaType);
    }

    public void setManaAmount(Mana manaType, int amount) {
        manaMap.put(manaType.registryHolder(), amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manaMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        return obj instanceof ManaDataComponent manaObj
                && Objects.equals(manaMap, manaObj.manaMap);
    }
}
