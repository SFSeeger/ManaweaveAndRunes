package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.spells.SpellPart;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit;
import io.github.sfseeger.manaweave_and_runes.core.util.ICreativeTabItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpellPartHolderItem extends Item implements ICreativeTabItem {
    private final boolean addToCreativeTab;

    public SpellPartHolderItem(Properties properties, boolean addToCreativeTab) {
        super(properties);
        this.addToCreativeTab = addToCreativeTab;
    }

    @Override
    public Component getName(ItemStack stack) {
        SpellPart spellPart = stack.get(MRDataComponentsInit.SPELL_PART_DATA_COMPONENT);
        MutableComponent name = Component.translatable("item.manaweave_and_runes.spell_part_holder");
        if (spellPart != null) name.append(": ").append(spellPart.getCore().value().getName());

        return name;
    }

    @Override
    public boolean isInCreativeTab() {
        return addToCreativeTab;
    }
}
