package io.github.sfseeger.lib.common.spells;

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
}
