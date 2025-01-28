package io.github.sfseeger.lib.common.items;

import io.github.sfseeger.lib.common.spells.AbstractSpellEffect;
import io.github.sfseeger.lib.common.spells.Spell;
import io.github.sfseeger.lib.common.spells.SpellDataComponent;
import io.github.sfseeger.lib.common.spells.buildin.effects.SpellEffectBurn;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import io.github.sfseeger.lib.common.spells.buildin.types.SpellTypeProjectile;
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
        super(properties.stacksTo(1)
                .component(ManaweaveAndRunesDataComponentsInit.SPELL_DATA_COMPOENTN, new SpellDataComponent(s1)));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        SpellDataComponent component = stack.get(ManaweaveAndRunesDataComponentsInit.SPELL_DATA_COMPOENTN);
        if (component != null) {
            tooltipComponents.add(Component.literal(component.spell().getName()));
            tooltipComponents.add(component.spell().getSpellType().getName().withColor(0xFF0000));
            for (AbstractSpellEffect effect : component.spell().getEffects()) {
                MutableComponent effectText = effect.getName().withColor(0x00FF00);
                for (var modifier : component.spell().getModifiers().get(effect)) {
                    effectText.append(", ").withColor(0x0000FF).append(modifier.getName().withColor(0x0000FF));
                }
                tooltipComponents.add(effectText);
            }
        }
    }


}
