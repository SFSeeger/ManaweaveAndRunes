package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class SpellCastingContext implements Cloneable {
    private final Level level;
    private final LivingEntity caster;
    private final InteractionHand handIn;
    private final Map<String, Object> variables =
            new HashMap<>(Map.of("strength", 1f, "duration", 1f, "modifier_cooldown_multiplier", 1f));

    public SpellCastingContext(Level level, LivingEntity caster, InteractionHand handIn) {
        this.level = level;
        this.caster = caster;
        this.handIn = handIn;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
    public Object getVariable(String key) {return variables.get(key);}

    public Level getLevel() {
        return level;
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }

    @Override
    public SpellCastingContext clone() {
        try {
            SpellCastingContext clone = (SpellCastingContext) super.clone();
            clone.variables.putAll(variables);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }
}
