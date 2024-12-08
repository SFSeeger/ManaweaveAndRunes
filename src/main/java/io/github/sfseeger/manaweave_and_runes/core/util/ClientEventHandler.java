package io.github.sfseeger.manaweave_and_runes.core.util;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

// If you don't need any clientEventHandlers, you can delete this file

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus=EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Add block entity renderers here
    }
}
