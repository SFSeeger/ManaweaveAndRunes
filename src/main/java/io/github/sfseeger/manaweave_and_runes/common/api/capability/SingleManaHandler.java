package io.github.sfseeger.manaweave_and_runes.common.api.capability;


import io.github.sfseeger.manaweave_and_runes.common.api.mana.Mana;

public class SingleManaHandler extends ManaHandler {
    public SingleManaHandler(Mana manatype, int capacity, int maxReceive, int maxExtract) {
        super(manatype, capacity, 0, maxReceive, maxExtract);
    }

    public SingleManaHandler(Mana manatype, int capacity, int stored,
            int maxReceive, int maxExtract) {
        super(manatype, capacity, stored, maxReceive, maxExtract);
    }

    @Override
    public boolean canExtract(Mana manatype) {
        if (!this.manaStored.isEmpty() && this.getMaxManaStored(manatype) == -1) {
            return false;
        }
        return super.canExtract(manatype);
    }

    @Override
    public boolean canReceive(Mana manatype) {
        if (!this.manaStored.isEmpty() && this.getMaxManaStored(manatype) == -1) {
            return false;
        }
        return super.canReceive(manatype);
    }
}
