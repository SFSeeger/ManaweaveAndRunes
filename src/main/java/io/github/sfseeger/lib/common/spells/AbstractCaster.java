package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.spells.buildin.effects.SpellEffectBurn;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import io.github.sfseeger.lib.common.spells.buildin.types.SpellTypeProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AbstractCaster {
    public Spell currentSpell;

    public AbstractCaster() {
        currentSpell = new Spell(SpellTypeProjectile.INSTANCE,
                                 List.of(SpellEffectBurn.INSTANCE),
                                 Map.of(SpellEffectBurn.INSTANCE,
                                        List.of(SpellModifierStrengthen.INSTANCE,
                                                SpellModifierStrengthen.INSTANCE,
                                                SpellModifierStrengthen.INSTANCE,
                                                SpellModifierStrengthen.INSTANCE)));
    }

    public InteractionResultHolder<ItemStack> cast(Level level, LivingEntity entity, InteractionHand handIn,
            @NotNull Spell spell) {
        if (level.isClientSide()) return InteractionResultHolder.pass(entity.getItemInHand(handIn));

        HitResult result =
                SpellUtils.rayTrace(entity, 0.5 + entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue(), 0,
                                    false);
        AbstractSpellType type = spell.getSpellType();
        SpellCastingContext context = new SpellCastingContext(level, entity, handIn);
        SpellResolver resolver = new SpellResolver(spell);
        if (result instanceof BlockHitResult) {
            type.castOnBlock((BlockHitResult) result, context, resolver);
            return InteractionResultHolder.consume(entity.getItemInHand(handIn));
        }
        if (result instanceof EntityHitResult) {
            type.castOnEntity(((EntityHitResult) result).getEntity(), context, resolver);
            return InteractionResultHolder.consume(entity.getItemInHand(handIn));
        }
        if (type.cast(context, resolver).isSuccess()) {
            return InteractionResultHolder.consume(entity.getItemInHand(handIn));
        }
        return InteractionResultHolder.fail(entity.getItemInHand(handIn));
    }
}
