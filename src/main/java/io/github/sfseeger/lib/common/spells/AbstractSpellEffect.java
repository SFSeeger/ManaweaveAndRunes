package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSpellEffect extends AbstractSpellNode {
    public AbstractSpellEffect() {
        super();
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

    @Override
    public @NotNull SpellNodeType getSpellNodeType() {
        return SpellNodeType.EFFECT;
    }
}
