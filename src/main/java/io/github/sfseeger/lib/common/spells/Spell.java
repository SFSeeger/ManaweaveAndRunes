package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.mana.Mana;

import java.util.List;
import java.util.Map;

public class Spell {
    // TODO: Add codec to serialize into saved data
    private AbstractSpellType spellType;
    private List<AbstractSpellEffect> effects;
    private Map<AbstractSpellNode, AbstractSpellModifier[]> modifiers;

    public Spell(AbstractSpellType spellType, List<AbstractSpellEffect> effects, Map<AbstractSpellNode, AbstractSpellModifier[]> modifiers) {
        this.spellType = spellType;
        this.effects = effects;
        this.modifiers = modifiers;
    }

    public AbstractSpellNode getSpellType() {
        return spellType;
    }

    public List<AbstractSpellEffect> getEffects() {
        return effects;
    }

    public Map<AbstractSpellNode, AbstractSpellModifier[]> getModifiers() {
        return modifiers;
    }

    public Map<Mana, Integer> getManaCost() {
        Map<Mana, Integer> cost = spellType.getManaCost();
        for (AbstractSpellEffect effect : effects) {
            for (Map.Entry<Mana, Integer> entry : effect.getManaCost().entrySet()) {
                cost.put(entry.getKey(), cost.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        for(AbstractSpellModifier[] modifier : modifiers.values()) {
            for (AbstractSpellModifier m : modifier) {
                for (Map.Entry<Mana, Integer> entry : m.getManaCost().entrySet()) {
                    cost.put(entry.getKey(), cost.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }
            }
        }
        return cost;
    }

    public int getCooldown(){
        int cooldown = spellType.getCooldown();
        for (AbstractSpellEffect effect : effects) {
            cooldown += effect.getCooldown();
        }
        for(AbstractSpellModifier[] modifier : modifiers.values()) {
            for (AbstractSpellModifier m : modifier) {
                cooldown += m.getCooldown();
            }
        }
        return cooldown;
    }
}
