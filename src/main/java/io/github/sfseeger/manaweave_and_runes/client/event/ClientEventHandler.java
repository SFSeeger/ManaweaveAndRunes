package io.github.sfseeger.manaweave_and_runes.client.event;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.client.renderer.ManaCollectorBlockEntityRenderer;
import io.github.sfseeger.manaweave_and_runes.client.screens.RuneCarverBlockScreen;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus=EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ManaweaveAndRunesBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(),
                                          ManaCollectorBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ManaweaverAndRunesMenuInit.RUNE_CARVER_BLOCK_MENU.get(), RuneCarverBlockScreen::new);
    }
}
