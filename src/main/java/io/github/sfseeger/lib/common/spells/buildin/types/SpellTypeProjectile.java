package io.github.sfseeger.lib.common.spells.buildin.types;

import io.github.sfseeger.lib.common.entities.projectiles.SpellProjectileEntity;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;
import java.util.Set;

public class SpellTypeProjectile extends AbstractSpellType {
    public static final SpellTypeProjectile INSTANCE = new SpellTypeProjectile();

    public SpellTypeProjectile() {
        super(Map.of(Manas.AirMana, 5), 10);
    }

    @Override
    public SpellCastingResult cast(SpellCastingContext context, SpellResolver resolver) {
        createProjectile(context, resolver);
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public SpellCastingResult castOnBlock(BlockHitResult result, SpellCastingContext context, SpellResolver resolver) {
        createProjectile(context, resolver);
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public SpellCastingResult castOnEntity(Entity target, SpellCastingContext context, SpellResolver resolver) {
        createProjectile(context, resolver);
        return SpellCastingResult.SUCCESS;
    }

    private void createProjectile(SpellCastingContext context, SpellResolver resolver) {
        Level level = context.getLevel();
        LivingEntity caster = context.getCaster();
        SpellProjectileEntity entity = new SpellProjectileEntity(level, context, resolver);
        entity.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0F, 2.5F, 1.0F);
        level.addFreshEntity(entity);
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of();
    }
}
