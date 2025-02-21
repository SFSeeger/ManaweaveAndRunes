package io.github.sfseeger.lib.common.spells.buildin.effects;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierElongate;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierWiden;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Set;

import static net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR;

public class SpellEffectPush extends AbstractSpellEffect {
    public static final SpellEffectPush INSTANCE = new SpellEffectPush();

    public SpellEffectPush() {
        super(Map.of(Manas.AirMana, 5), 5);
    }

    @Override
    public SpellCastingResult resolveBlock(BlockHitResult blockHitResult, SpellCastingContext context) {
        Level level = context.getLevel();
        BlockPos pos = blockHitResult.getBlockPos().above();
        float strength = (Float) context.getVariable("strength") / 1.5f;

        SpellUtils.executeOnPlane(pos, context, blockHitResult.getDirection(), 2, (pos1) -> {
            level.explode(null, null, EXPLOSION_DAMAGE_CALCULATOR, pos1.getX(), pos1.getY(), pos1.getZ(),
                          3.0F * strength, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL,
                          ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.BREEZE_WIND_CHARGE_BURST);
            return true;
        });

        return SpellCastingResult.SUCCESS;
    }

    @Override
    public SpellCastingResult resolveEntity(EntityHitResult entityHitResult, SpellCastingContext context) {
        Vec3 look = context.getCaster().getLookAngle();
        Entity entity = entityHitResult.getEntity();
        float strength = Math.min((Float) context.getVariable("strength") / 2, 1);
        int width = context.getVariableSave("width", 1);
        int height = context.getVariableSave("height", 1);

        entity.addDeltaMovement(look.scale(2).multiply(new Vec3(width, height, width).scale(strength)));
        entity.hurtMarked = true;
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of(SpellModifierStrengthen.INSTANCE, SpellModifierWiden.INSTANCE, SpellModifierElongate.INSTANCE);
    }
}
