package io.github.sfseeger.lib.common.spells;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.mana.Mana;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Spell {
    // TODO: Add codec to serialize into saved data
    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AbstractSpellType.CODEC.fieldOf("spellType").forGetter(Spell::getSpellType),
            AbstractSpellEffect.CODEC.listOf().fieldOf("effects").forGetter(Spell::getEffectAsNodes),
            Codec.pair(AbstractSpellNode.CODEC, AbstractSpellModifier.CODEC.listOf()).listOf()
                    .fieldOf("modifiers")
                    .forGetter(Spell::getModifiersAsPairs)
    ).apply(instance, Spell::fromCodec));

    private AbstractSpellType spellType;
    private List<AbstractSpellEffect> effects;
    private Map<AbstractSpellNode, List<AbstractSpellModifier>> modifiers;
    private String name = "Unnamed Spell";

    public Spell(AbstractSpellType spellType, List<AbstractSpellEffect> effects,
            Map<AbstractSpellNode, List<AbstractSpellModifier>> modifiers) {
        this.spellType = spellType;
        this.effects = effects;
        this.modifiers = modifiers;
    }

    public static Spell fromCodec(AbstractSpellNode type, List<AbstractSpellNode> effects,
            List<Pair<AbstractSpellNode, List<AbstractSpellNode>>> modifiers) {
        Map<AbstractSpellNode, List<AbstractSpellModifier>> modifierMap = modifiers.stream().collect(Collectors.toMap(
                Pair::getFirst,
                pair -> pair.getSecond().stream().map(modifier -> (AbstractSpellModifier) modifier).toList()));
        return new Spell((AbstractSpellType) type,
                         effects.stream().map(effect -> (AbstractSpellEffect) effect).toList(), modifierMap);
    }

    public AbstractSpellType getSpellType() {
        return spellType;
    }

    public List<AbstractSpellEffect> getEffects() {
        return effects;
    }

    public List<AbstractSpellNode> getEffectAsNodes() {
        return List.copyOf(effects);
    }

    public Map<AbstractSpellNode, List<AbstractSpellModifier>> getModifiers() {
        return modifiers;
    }

    public List<Pair<AbstractSpellNode, List<AbstractSpellNode>>> getModifiersAsPairs() {
        return modifiers.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), List.of(entry.getValue()).stream()
                        .map(modifier -> (AbstractSpellNode) modifier).toList()))
                .toList();
    }


    public Map<Mana, Integer> getManaCost() {
        Map<Mana, Integer> cost = spellType.getManaCost();
        for (AbstractSpellEffect effect : effects) {
            for (Map.Entry<Mana, Integer> entry : effect.getManaCost().entrySet()) {
                cost.put(entry.getKey(), cost.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        for (List<AbstractSpellModifier> modifier : modifiers.values()) {
            for (AbstractSpellModifier m : modifier) {
                for (Map.Entry<Mana, Integer> entry : m.getManaCost().entrySet()) {
                    cost.put(entry.getKey(), cost.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }
            }
        }
        return cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCooldown() {
        int cooldown = spellType.getCooldown();
        for (AbstractSpellEffect effect : effects) {
            cooldown += effect.getCooldown();
        }
        for (List<AbstractSpellModifier> modifier : modifiers.values()) {
            for (AbstractSpellModifier m : modifier) {
                cooldown += m.getCooldown();
            }
        }
        return cooldown;
    }
}
