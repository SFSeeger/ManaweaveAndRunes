package io.github.sfseeger.lib.common.mana.capability;

import io.github.sfseeger.lib.common.mana.Mana;

import java.util.Set;

public interface IManaHandler {
    int receiveMana(int amount, Mana manatype, boolean simulate);

    int extractMana(int amount, Mana manatype, boolean simulate);

    int getManaStored(Mana manatype);

    int getManaCapacity();

    boolean canExtract(Mana manatype);

    boolean canReceive(Mana manatype);

    Set<Mana> getManaTypesStored();

    boolean hasMana(Mana manatype);
}