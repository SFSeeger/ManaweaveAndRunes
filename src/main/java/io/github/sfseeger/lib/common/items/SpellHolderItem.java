package io.github.sfseeger.lib.common.items;

import io.github.sfseeger.lib.common.spells.Spell;
import io.github.sfseeger.lib.common.spells.data_components.SpellDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpellHolderItem extends Item {
    public SpellHolderItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static @Nullable Spell getSpell(ItemStack stack) {
        SpellDataComponent component = stack.get(MRDataComponentsInit.SPELL_DATA_COMPONENT);
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
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return super.getTooltipImage(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        Spell spell = getSpell(stack);
        MutableComponent name = super.getName(stack).plainCopy();
        if (spell != null) {
            return name.append(": ").append(spell.getName());
        }
        return name;
    }
}
