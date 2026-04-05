package io.github.sfseeger.manaweave_and_runes.client;

import io.github.sfseeger.manaweave_and_runes.core.payloads.CraftPayload;
import io.github.sfseeger.manaweave_and_runes.core.payloads.CameraSetPayload;
import io.github.sfseeger.manaweave_and_runes.core.payloads.SwitchSpellPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class ClientPayloadHandler {
    public static void handleCraftPayload(CraftPayload data, IPayloadContext context) {
        System.out.println("Client received craft payload: " + data);
    }

    public static void handleSpellSwitchPayload(SwitchSpellPayload data, IPayloadContext context) {
        System.out.println("Client received spell switch payload: " + data);
    }

    public static void handlePlayerViewPayload(CameraSetPayload payload, IPayloadContext context) {
        System.out.println("Client received player view payload: " + payload);
        Player player = context.player();
        Entity entityToView = player.level().getEntity(payload.entityUUID());
        if (entityToView != null) {
            System.out.println("Found Player " + player);
            Minecraft.getInstance().setCameraEntity(entityToView);
        }
    }
}
