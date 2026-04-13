package io.github.sfseeger.manaweave_and_runes.common.spells.types;

import io.github.sfseeger.lib.common.entities.projectiles.SpellProjectileEntity;
import io.github.sfseeger.lib.common.spells.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class SpellTypeProjectile extends AbstractSpellType {
    public static final SpellTypeProjectile INSTANCE = new SpellTypeProjectile();

    public SpellTypeProjectile() {
        super();
    }

    @Override
    public SpellCastingResult cast(AbstractSpellCastingContext context, SpellResolver resolver) {
        createProjectile(context, resolver);
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public SpellCastingResult castOnBlock(BlockHitResult result, AbstractSpellCastingContext context, SpellResolver resolver) {
        createProjectile(context, resolver);
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public SpellCastingResult castOnEntity(Entity target, AbstractSpellCastingContext context, SpellResolver resolver) {
        createProjectile(context, resolver);
        return SpellCastingResult.SUCCESS;
    }

    private void createProjectile(AbstractSpellCastingContext context, SpellResolver resolver) {
        Level level = context.getLevel();
        LivingEntity caster = context.getCaster();
        if (level == null || caster == null) return;
        SpellProjectileEntity entity = new SpellProjectileEntity(level, context, resolver);
        float strength = context.getFloatContextData("strength", 1f);
        entity.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0F, 2.5F + strength / 2, 1.0F);
        level.addFreshEntity(entity);
    }
}
