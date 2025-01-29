package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public interface ISpellCaster {
    IItemHandler getItemHandler(ItemStack stack);

    void switchSpell(ItemStack stack, int spellIndex);

    int getSlotCount();

    int getCurrentSpellIndex(ItemStack stack);

    Spell getCurrrntSpell(ItemStack stack);
}
