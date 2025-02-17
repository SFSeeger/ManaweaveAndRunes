package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.item.ItemStack;

public interface ISpellCaster {

    void switchSpell(ItemStack stack, int spellIndex);

    int getCurrentSpellIndex(ItemStack stack);

    Spell getCurrrntSpell(ItemStack stack);

    Spell getSpell(ItemStack stack, int index);

    int getNextSpellIndex(ItemStack stack, int currentSpellIndex);
}
