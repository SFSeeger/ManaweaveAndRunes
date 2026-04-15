package io.github.sfseeger.manaweave_and_runes.common.spells.effects;

import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import io.github.sfseeger.lib.common.spells.AbstractSpellEffect;
import io.github.sfseeger.lib.common.spells.SpellCastingResult;
import io.github.sfseeger.lib.common.spells.SpellUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class SpellEffectExplode extends AbstractSpellEffect {
    public static final SpellEffectExplode INSTANCE = new SpellEffectExplode();

    public SpellEffectExplode() {
        super();
    }

    @Override
    public @NotNull SpellCastingResult resolveBlock(BlockHitResult blockHitResult, AbstractSpellCastingContext context) {
        return explode(blockHitResult.getBlockPos(), context) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
    }

    @Override
    public @NotNull SpellCastingResult resolveEntity(EntityHitResult entityHitResult, AbstractSpellCastingContext context) {
        return explode(entityHitResult.getEntity().getOnPos(),
                       context) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
    }

    private boolean explode(BlockPos pos, AbstractSpellCastingContext context) {
        Level level = context.getLevel();
        LivingEntity entity = context.getCaster();
        float strength = context.getFloatContextData("strength", 1f) / 1.5f;
        if (SpellUtils.canChangeBlockState(pos, context)) {
            boolean isDelicate = context.getBooleanContextData("delicate");
            level.explode(null,
                          Explosion.getDefaultDamageSource(level, entity),
                          null,
                          pos.getCenter(),
                          3 + strength,
                          false,
                          isDelicate ? Level.ExplosionInteraction.NONE : Level.ExplosionInteraction.TNT);
            return true;
        }
        return false;
    }
}
