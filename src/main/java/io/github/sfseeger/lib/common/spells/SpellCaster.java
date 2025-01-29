package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class SpellCaster {

    public SpellCaster() {
    }

    public SpellCastingResult cast(Level level, LivingEntity entity, InteractionHand handIn, @NotNull Spell spell) {
        if (level.isClientSide()) return SpellCastingResult.SKIPPED;

        HitResult result =
                SpellUtils.rayTrace(entity, 0.5 + entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue(), 0,
                                    false);
        AbstractSpellType type = spell.getSpellType();
        SpellCastingContext context = new SpellCastingContext(level, entity, handIn);
        SpellResolver resolver = new SpellResolver(spell);
        if (result instanceof BlockHitResult) {
            return type.castOnBlock((BlockHitResult) result, context, resolver);
        }
        if (result instanceof EntityHitResult) {
            return type.castOnEntity(((EntityHitResult) result).getEntity(), context, resolver);
        }
        return type.cast(context, resolver);
    }
}
