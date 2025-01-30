package io.github.sfseeger.manaweave_and_runes.core.payloads;

import net.minecraft.world.entity.player.Player;

public interface ICraftingPacketHandler {
    void onPacketReceive(CraftPayload payload, Player player);
}
