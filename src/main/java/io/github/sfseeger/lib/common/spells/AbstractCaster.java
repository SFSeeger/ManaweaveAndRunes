package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.manaweave_and_runes.core.init.SpellNodeInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AbstractCaster {
    Spell currentSpell;

    public AbstractCaster() {
        currentSpell = new Spell((AbstractSpellType) SpellNodeInit.SPELL_TYPE_TOUCH.get(),
                List.of((AbstractSpellEffect) SpellNodeInit.SPELL_EFFECT_BURN.get()), Map.of());
    }

    public InteractionResultHolder<ItemStack> cast(Level level, LivingEntity entity, InteractionHand handIn, @NotNull Spell spell){
        if(level.isClientSide()) return InteractionResultHolder.pass(entity.getItemInHand(handIn));

        HitResult result = SpellUtils.rayTrace(entity, 0.5 + entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue(), 0, false);
        return null;
    }
}
