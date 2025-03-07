package io.github.sfseeger.lib.common.spells.buildin.effects;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Map;
import java.util.Set;

public class SpellEffectExplode extends AbstractSpellEffect {
    public static final SpellEffectExplode INSTANCE = new SpellEffectExplode();

    public SpellEffectExplode() {
        super(Map.of(Manas.FireMana, 4, Manas.EntropyMana, 10), 10);
    }

    @Override
    public SpellCastingResult resolveBlock(BlockHitResult blockHitResult, SpellCastingContext context) {
        return explode(blockHitResult.getBlockPos(), context) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
    }

    @Override
    public SpellCastingResult resolveEntity(EntityHitResult entityHitResult, SpellCastingContext context) {
        return explode(entityHitResult.getEntity().getOnPos(),
                       context) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
    }

    private boolean explode(BlockPos pos, SpellCastingContext context) {
        Level level = context.getLevel();
        LivingEntity entity = context.getCaster();
        float strength = (Float) context.getVariable("strength") / 1.5f;
        if (SpellUtils.canChangeBlockState(pos, context)) {
            level.explode(null,
                          Explosion.getDefaultDamageSource(level, entity),
                          null,
                          pos.getCenter(),
                          3 + strength,
                          false,
                          Level.ExplosionInteraction.TNT);
            return true;
        }
        return false;
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of(SpellModifierStrengthen.INSTANCE);
    }
}
