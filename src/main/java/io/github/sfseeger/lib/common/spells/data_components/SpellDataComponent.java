package io.github.sfseeger.lib.common.spells.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.spells.Spell;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record SpellDataComponent(Spell spell) {
    public static final Codec<SpellDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Spell.CODEC.fieldOf("spell").forGetter(SpellDataComponent::spell)
    ).apply(instance, SpellDataComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellDataComponent> STREAM_CODEC = StreamCodec.composite(
            Spell.STREAM_CODEC, SpellDataComponent::spell, SpellDataComponent::new
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpellDataComponent that = (SpellDataComponent) o;
        return Objects.equals(spell, that.spell);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(spell);
    }
}
