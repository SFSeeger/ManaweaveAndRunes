package io.github.sfseeger.lib.common.mana;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public class ManaDateComponent {
    public static final Codec<ManaDateComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Mana.CODEC.fieldOf("manaType").forGetter(ManaDateComponent::getManaType),
            Codec.INT.fieldOf("manaAmount").forGetter(ManaDateComponent::getManaAmount)
    ).apply(instance, ManaDateComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ManaDateComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(ManaRegistry.MANA_REGISTRY_KEY), ManaDateComponent::getManaType,
            ByteBufCodecs.INT, ManaDateComponent::getManaAmount,
            ManaDateComponent::new
    );

    private final Holder<Mana> manaType;
    private final int manaAmount;


    public ManaDateComponent(Holder<Mana> manaType, int manaAmount) {
        this.manaType = manaType;
        this.manaAmount = manaAmount;
    }

    public ManaDateComponent(Mana manaType, int manaAmount) {
        this.manaType = manaType.registryHolder();
        this.manaAmount = manaAmount;
    }


    public Holder<Mana> getManaType() {
        return manaType;
    }

    public int getManaAmount() {
        return manaAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.manaType, this.manaAmount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        return obj instanceof ManaDateComponent
                && ((ManaDateComponent) obj).manaType == this.manaType
                && ((ManaDateComponent) obj).manaAmount == this.manaAmount;
    }
}
