package io.github.sfseeger.lib.common.spells.casting_context;

import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntitySpellCastingContext extends AbstractSpellCastingContext {
    public EntitySpellCastingContext(LivingEntity caster) {
        this.caster = caster;

        // Initialize fields
        getCasterId();
        getCasterUUID();
    }

    @Override
    public Vec3 getPosition() {
        return caster.position();
    }

    @Override
    public Vec3 getLookDirection() {
        return caster.getLookAngle();
    }

    @Override
    public Direction getDirection() {
        return caster.getDirection();
    }

    @Override
    public @Nullable Level getLevel() {
        return caster.level();
    }


    @Override
    public ContextMap getContextData() {
        return contextData;
    }

    @Override
    public EntitySpellCastingContext clone() throws CloneNotSupportedException {
        return (EntitySpellCastingContext) super.clone();
    }
}
