package io.github.sfseeger.lib.common.mana.network;

import io.github.sfseeger.lib.common.mana.Mana;

public record ManaTransaction(ManaNetworkNode node, Mana mana, int amount) {
    public ManaTransaction {
        assert amount > 0;
    }
}
