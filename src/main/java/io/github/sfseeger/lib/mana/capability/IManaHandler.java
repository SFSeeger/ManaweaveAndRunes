package io.github.sfseeger.lib.mana.capability;

import io.github.sfseeger.lib.mana.Mana;

import java.util.Set;

public interface IManaHandler {
    int receiveMana(int amount, Mana manatype, boolean simulate);

    int extractMana(int amount, Mana manatype, boolean simulate);

    int getManaStored(Mana manatype);

    int getMaxManaStored(Mana manatype);

    boolean canExtract(Mana manatype);

    boolean canReceive(Mana manatype);

    Set<Mana> getManaTypesStored();

    boolean hasMana(Mana manatype);
}
