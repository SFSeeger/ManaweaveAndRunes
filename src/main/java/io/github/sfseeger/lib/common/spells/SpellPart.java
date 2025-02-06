package io.github.sfseeger.lib.common.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpellPart {
    public static final Codec<SpellPart> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbstractSpellNode.HOLDER_CODEC.fieldOf("core").forGetter(SpellPart::getCore),
            AbstractSpellModifier.CODEC.listOf().fieldOf("modifiers").forGetter(SpellPart::getModifiersAsNodes)
    ).apply(instance, SpellPart::fromCodec));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellPart> STREAM_CODEC = StreamCodec.composite(
            AbstractSpellNode.HOLDER_STREAM_CODEC, SpellPart::getCore,
            AbstractSpellNode.STREAM_CODEC.apply(ByteBufCodecs.list(16)), SpellPart::getModifiersAsNodes,
            SpellPart::fromCodec
    );

    private final Holder<AbstractSpellNode> core;
    private List<AbstractSpellModifier> modifiers = new ArrayList<>();

    public SpellPart(Holder<AbstractSpellNode> core, List<AbstractSpellModifier> modifiers) {
        this(core);
        this.modifiers = modifiers;
    }

    public SpellPart(Holder<AbstractSpellNode> core) {
        this.core = core;
    }

    private static SpellPart fromCodec(Holder<AbstractSpellNode> core, List<AbstractSpellNode> modifiers) {
        return new SpellPart(core, modifiers.stream().map(n -> (AbstractSpellModifier) n).collect(Collectors.toList()));
    }

    public Holder<AbstractSpellNode> getCore() {
        return core;
    }

    public List<AbstractSpellModifier> getModifiers() {
        return modifiers;
    }

    public List<AbstractSpellNode> getModifiersAsNodes() {
        return modifiers.stream().map(n -> (AbstractSpellNode) n).collect(Collectors.toList());
    }

    public int getCooldown(){
        return modifiers.stream().mapToInt(AbstractSpellModifier::getCooldown).sum() + core.value().getCooldown();
    }

    @Override
    public int hashCode() {
        return Objects.hash(core, modifiers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpellPart spellPart)) return false;
        return Objects.equals(core, spellPart.core) && Objects.equals(modifiers, spellPart.modifiers);
    }
}
