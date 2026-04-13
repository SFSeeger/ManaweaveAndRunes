package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.lib.common.spells.casting_context.EntitySpellCastingContext;
import io.github.sfseeger.manaweave_and_runes.core.init.MRTagInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
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

    public static SpellCastingResult cast(Level level, LivingEntity entity, InteractionHand handIn, @NotNull Spell spell) {
        AbstractSpellCastingContext context = new EntitySpellCastingContext(entity);
        SpellResolver resolver = new SpellResolver(spell);

        SpellPart core = spell.getCore();
        core.getModifiers().forEach(modifier -> modifier.onGatherContext(null, context));
        core.getModifiers().forEach(modifier -> modifier.preResolve(null, context));

        if (!extractRequiredMana(spell, context, true)) return SpellCastingResult.FAILURE;
        extractRequiredMana(spell, context, false);

        if (!level.isClientSide() && core.getCore().value() instanceof AbstractSpellType type) {
            HitResult result =
                    SpellUtils.rayTrace(entity,
                                        0.3f + entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue(),
                                        0,
                                        false);

            if (result instanceof BlockHitResult r && r.getType() != HitResult.Type.MISS) {
                return type.castOnBlock(r, context, resolver);
            }
            if (result instanceof EntityHitResult r && r.getType() != HitResult.Type.MISS) {
                return type.castOnEntity(r.getEntity(), context, resolver);
            }
            return type.cast(context, resolver);
        }
        return SpellCastingResult.SKIPPED;
    }

    public static boolean extractRequiredMana(Spell spell, AbstractSpellCastingContext context, boolean simulate) {
        if(context.getCaster() == null) return false;
        if(context.getCaster().hasInfiniteMaterials()) return true;

        Map<Mana, Integer> requiredMana = spell.getManaCost();
        Map<Mana, Integer> consumedMana = new HashMap<>();
        Iterable<ItemStack> items;
        boolean isPlayer = context.getCaster() instanceof Player;
        if (isPlayer) {
            items = ((Player) context.getCaster()).getInventory().items.stream()
                    .filter(itemStack -> itemStack.is(MRTagInit.SPELL_MANA_PROVIDER))
                    .toList();
        } else {
            items = context.getCaster().getAllSlots();
        }
        for (ItemStack itemStack : items) {
            IManaHandler handler = itemStack.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
            if (handler != null) {
                for (Mana mana : requiredMana.keySet()) {
                    int toConsume = requiredMana.get(mana) - consumedMana.getOrDefault(mana, 0);
                    if (toConsume > 0 && handler.hasMana(mana)) {
                        int received = handler.extractMana(toConsume, mana, simulate);
                        if (received > 0) consumedMana.put(mana, consumedMana.getOrDefault(mana, 0) + received);
                    }
                }
                if (consumedMana.equals(requiredMana)) return true;
            }
        }
        return false;
    }
}
