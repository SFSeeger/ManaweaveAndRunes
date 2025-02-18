package io.github.sfseeger.lib.common.items;

import io.github.sfseeger.lib.common.spells.SpellPart;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpellPartHolderItem extends Item {
    public SpellPartHolderItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        SpellPart spellPart = stack.get(ManaweaveAndRunesDataComponentsInit.SPELL_PART_DATA_COMPONENT);
        MutableComponent name = Component.translatable("item.manaweave_and_runes.spell_part_holder");
        if (spellPart != null) name.append(": ").append(spellPart.getCore().value().getName());

        return name;
    }
}
