package io.github.sfseeger.manaweave_and_runes.common.api.capability;

import io.github.sfseeger.manaweave_and_runes.common.api.mana.Mana;

public interface IManaHandler {
    int receiveMana(int amount, Mana manatype, boolean simulate);

    int extractMana(int amount, Mana manatype, boolean simulate);

    int getManaStored(Mana manatype);

    int getMaxManaStored(Mana manatype);

    boolean canExtract(Mana manatype);

    boolean canReceive(Mana manatype);
}
