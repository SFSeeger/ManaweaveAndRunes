package io.github.sfseeger.manaweave_and_runes.client;

import io.github.sfseeger.manaweave_and_runes.core.payloads.CraftPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleCraftPayload(CraftPayload data, IPayloadContext context) {
        System.out.println("Client received craft payload: " + data);
    }
}
