package io.github.sfseeger.manaweave_and_runes.common;

import io.github.sfseeger.manaweave_and_runes.core.payloads.CraftPayload;
import io.github.sfseeger.manaweave_and_runes.core.payloads.ICraftingPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void handleCraftPayload(CraftPayload craftPayload, IPayloadContext context) {
        BlockEntity be = context.player().level().getBlockEntity(BlockPos.of(craftPayload.blockPos()));
        if (be instanceof ICraftingPacketHandler handler) {
            handler.onPacketReceive(craftPayload, context.player());
        }
    }
}
