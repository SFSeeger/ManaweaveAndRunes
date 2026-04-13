package io.github.sfseeger.manaweave_and_runes.common.spells.effects;

import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.manaweave_and_runes.common.spells.modifiers.SpellModifierElongate;
import io.github.sfseeger.manaweave_and_runes.common.spells.modifiers.SpellModifierStrengthen;
import io.github.sfseeger.manaweave_and_runes.common.spells.modifiers.SpellModifierWiden;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR;

public class SpellEffectPush extends AbstractSpellEffect {
    public static final SpellEffectPush INSTANCE = new SpellEffectPush();

    public SpellEffectPush() {
        super();
    }

    @Override
    public @NotNull SpellCastingResult resolveBlock(BlockHitResult blockHitResult, AbstractSpellCastingContext context) {
        Level level = context.getLevel();
        BlockPos pos = blockHitResult.getBlockPos().above();
        float strength = (Float) context.getFloatContextData("strength", 1f) / 1.5f;

        SpellUtils.executeOnPlane(pos, context, blockHitResult.getDirection(), 2, (pos1) -> {
            level.explode(null, null, EXPLOSION_DAMAGE_CALCULATOR, pos1.getX(), pos1.getY(), pos1.getZ(),
                          3.0F * strength, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL,
                          ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.BREEZE_WIND_CHARGE_BURST);
            return true;
        });

        return SpellCastingResult.SUCCESS;
    }

    @Override
    public @NotNull SpellCastingResult resolveEntity(EntityHitResult entityHitResult, AbstractSpellCastingContext context) {
        Vec3 look = context.getLookDirection();
        Entity entity = entityHitResult.getEntity();
        float strength = Math.min(context.getFloatContextData("strength", 1f) / 2, 1);
        int width = (int) context.getFloatContextData("width", 1);
        int height = (int) context.getFloatContextData("height", 1);

        entity.addDeltaMovement(look.scale(2).multiply(new Vec3(width, height, width).scale(strength)));
        entity.hurtMarked = true;
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of(SpellModifierStrengthen.INSTANCE, SpellModifierWiden.INSTANCE, SpellModifierElongate.INSTANCE);
    }
}
