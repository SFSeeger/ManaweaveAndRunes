package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class SpellCastingContext {
    private final Level level;
    private final LivingEntity caster;
    private final Map<String, Object> variables = new HashMap<>();

    public SpellCastingContext(Level level, LivingEntity caster) {
        this.level = level;
        this.caster = caster;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public Level getLevel() {
        return level;
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }
}
