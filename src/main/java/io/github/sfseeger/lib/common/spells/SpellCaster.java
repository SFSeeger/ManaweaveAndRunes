package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.IManaItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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

    public boolean extractRequiredMana(Spell spell, SpellCastingContext context, boolean simulate){
        if(context.getCaster().hasInfiniteMaterials()) return true;

        Map<Mana, Integer> requiredMana = spell.getManaCost();
        Map<Mana, Integer> consumedMana = new HashMap<>();
        for(ItemStack itemStack : context.getCaster().getAllSlots()){
            if(itemStack.getItem() instanceof IManaItem manaItem){
                IManaHandler handler = manaItem.getManaHandler(itemStack);
                for(Mana mana : requiredMana.keySet()){
                    int toConsume = requiredMana.get(mana) - consumedMana.getOrDefault(mana, 0);
                    if(toConsume > 0 && manaItem.getManaHandler(itemStack).hasMana(mana)){
                        int received = handler.extractMana(toConsume, mana, simulate);
                        if(received > 0) consumedMana.put(mana, consumedMana.getOrDefault(mana, 0) + received);
                    }
                }
            }
            if(consumedMana.equals(requiredMana)) return true;
        }
        return false;
    }
}
