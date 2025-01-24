package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public abstract class AbstractSpellEffect extends AbstractSpellNode {
    public AbstractSpellEffect(Map<Mana, Integer> baseCosts, int baseCooldown) {
        super(baseCosts, baseCooldown);
    }

    public SpellCastingResult resolve(HitResult rayTrace, SpellCastingContext context) {
        if (rayTrace instanceof BlockHitResult blockHitResult) {
            return resolveBlock(blockHitResult, context);
        } else if (rayTrace instanceof EntityHitResult entityHitResult) {
            return resolveEntity(entityHitResult, context);
        }
        return SpellCastingResult.FAILURE;
    }

    public abstract SpellCastingResult resolveBlock(BlockHitResult blockHitResult, SpellCastingContext context);

    public abstract SpellCastingResult resolveEntity(EntityHitResult entityHitResult, SpellCastingContext context);
}
