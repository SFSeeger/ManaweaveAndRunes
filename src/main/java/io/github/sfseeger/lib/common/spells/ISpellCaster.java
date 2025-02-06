package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public interface ISpellCaster {

    void switchSpell(ItemStack stack, int spellIndex);

    int getCurrentSpellIndex(ItemStack stack);

    Spell getCurrrntSpell(ItemStack stack);
}
