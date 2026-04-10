package io.github.sfseeger.lib.common.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.HitResult;

import java.util.*;
import java.util.stream.Collectors;

public class SpellPart implements Cloneable {
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

    public SpellCastingResult resolveEffect(HitResult hitResult, SpellCastingContext context) {
        SpellCastingContext localContext = context.clone();
        modifiers.forEach(modifier -> {
            modifier.onGatherContext(hitResult, localContext);
        });
        modifiers.forEach(modifier -> {
            modifier.preResolve(hitResult, context);
        });
        if (core.value() instanceof AbstractSpellEffect effect) {
            SpellCastingResult result = effect.resolve(hitResult, context);
            modifiers.forEach(modifier -> {
                modifier.postResolve(hitResult, context);
            });
            return result;
        }
        return SpellCastingResult.SKIPPED;
    }

    public int getCooldown() {
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

    public boolean isValid() {
        if (getSpellNodeType() == SpellNodeType.MODIFIER) return false;
        return core.value().getPossibleModifiers().containsAll(modifiers);
    }

    private float getManaCostMultiplier(int index) {
        int ring = index / 5;
        return 1 + ring * 0.5f;
    }

    public Map<Mana, Integer> getManaCost() {
        Map<Mana, Integer> manaCost = new HashMap<>(core.value().getManaCost());
        for (int i = 0; i < modifiers.size(); i++) {
            float costMultiplier = getManaCostMultiplier(i);
            modifiers.get(i)
                    .getManaCost()
                    .forEach((key, value) ->
                                     manaCost.merge(key, value != null ? (int) Math.ceil(
                                             value.floatValue() * costMultiplier) : 0, Integer::sum)
                    );
        }
        return manaCost;
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

    public SpellNodeType getSpellNodeType() {
        return core.value().getSpellNodeType();
    }

    @Override
    public SpellPart clone() {
        try {
            SpellPart clone = (SpellPart) super.clone();
            clone.modifiers = new ArrayList<>(modifiers);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
