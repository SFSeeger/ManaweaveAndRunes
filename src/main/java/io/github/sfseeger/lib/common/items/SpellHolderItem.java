package io.github.sfseeger.lib.common.items;

import io.github.sfseeger.lib.common.spells.AbstractSpellEffect;
import io.github.sfseeger.lib.common.spells.Spell;
import io.github.sfseeger.lib.common.spells.buildin.effects.SpellEffectBurn;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import io.github.sfseeger.lib.common.spells.buildin.types.SpellTypeProjectile;
import io.github.sfseeger.lib.common.spells.data_components.SpellDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Map;

public class SpellHolderItem extends Item {
    public static final Spell s1 = new Spell("Range Spell", SpellTypeProjectile.INSTANCE, List.of(SpellEffectBurn.INSTANCE), Map.of(SpellEffectBurn.INSTANCE, List.of(SpellModifierStrengthen.INSTANCE, SpellModifierStrengthen.INSTANCE, SpellModifierStrengthen.INSTANCE, SpellModifierStrengthen.INSTANCE)));

    public SpellHolderItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static Spell getSpell(ItemStack stack) {
        SpellDataComponent component = stack.get(ManaweaveAndRunesDataComponentsInit.SPELL_DATA_COMPONENT);
        if (component != null) {
            return component.spell();
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        Spell spell = getSpell(stack);
        if (spell != null) {
            tooltipComponents.add(Component.literal(spell.getName()));
            tooltipComponents.add(spell.getSpellType().getName().withColor(0xFF0000));
            for (AbstractSpellEffect effect : spell.getEffects()) {
                MutableComponent effectText = effect.getName().withColor(0x00FF00);
                if (spell.getModifiers().get(effect) != null && !spell.getModifiers().get(effect).isEmpty()) {
                    for (var modifier : spell.getModifiers().get(effect)) {
                        effectText.append(", ").withColor(0x0000FF).append(modifier.getName().withColor(0x0000FF));
                    }
                }
                tooltipComponents.add(effectText);
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        Spell spell = getSpell(stack);
        MutableComponent name = super.getName(stack).plainCopy();
        if (spell != null) {
            return name.append(" :").append(spell.getName());
        }
        return name;
    }
}
