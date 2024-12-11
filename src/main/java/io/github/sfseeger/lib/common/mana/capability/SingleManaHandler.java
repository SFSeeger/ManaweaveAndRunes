package io.github.sfseeger.lib.common.mana.capability;


import io.github.sfseeger.lib.common.mana.Mana;

import java.util.List;

public class SingleManaHandler extends ManaHandler {
    public SingleManaHandler(int capacity, int maxManaReceive, int maxManaExtract) {
        super(capacity, maxManaReceive, maxManaExtract, null);
    }

    public SingleManaHandler(int capacity, int maxManaReceive, int maxManaExtract, Mana manaType) {
        super(capacity, maxManaReceive, maxManaExtract, manaType != null ? List.of(manaType) : null);
    }


    @Override
    public boolean canExtract(Mana manatype) {
        if (!this.manaStored.isEmpty() && this.getManaStored(manatype) == -1) {
            return false;
        }
        return super.canExtract(manatype);
    }

    @Override
    public boolean canReceive(Mana manatype) {
        if (!this.manaStored.isEmpty() && this.getManaStored(manatype) == -1) {
            return false;
        }
        return super.canReceive(manatype);
    }
}
