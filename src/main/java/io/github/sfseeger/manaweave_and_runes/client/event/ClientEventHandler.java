package io.github.sfseeger.manaweave_and_runes.client.event;

import io.github.sfseeger.lib.client.ber.ManaNodeRenderer;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.client.particles.ManaConcentratedParticle;
import io.github.sfseeger.manaweave_and_runes.client.particles.ManaParticle;
import io.github.sfseeger.manaweave_and_runes.client.renderers.*;
import io.github.sfseeger.manaweave_and_runes.client.screens.ManaStorageBlockScreen;
import io.github.sfseeger.manaweave_and_runes.client.screens.RuneCarverBlockScreen;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaStorageBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.MRParticleTypeInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ManaweaveAndRunesBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(),
                                          ManaCollectorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ManaweaveAndRunesBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get(),
                                          RunePedestalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ManaweaveAndRunesBlockEntityInit.MANA_CONCENTRATOR_BLOCK_ENTITY.get(),
                                          ManaConcentratorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ManaweaveAndRunesBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(),
                                          ManaNodeRenderer::new);
        event.registerBlockEntityRenderer(ManaweaveAndRunesBlockEntityInit.RITUAL_ANCHOR_BLOCK_ENTITY.get(),
                                          ManaNodeRenderer::new);
        event.registerBlockEntityRenderer(ManaweaveAndRunesBlockEntityInit.MANA_TRANSMITTER_BLOCK_ENTITY.get(),
                                          ManaNodeRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ManaweaverAndRunesMenuInit.RUNE_CARVER_BLOCK_MENU.get(), RuneCarverBlockScreen::new);
        event.register(ManaweaverAndRunesMenuInit.MANA_STORAGE_BLOCK_MENU.get(), ManaStorageBlockScreen::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MRParticleTypeInit.MANA_PARTICLE.get(), ManaParticle.ManaParticleProvider::new);
        event.registerSpriteSet(MRParticleTypeInit.MANA_CONCENTRATED.get(),
                                ManaConcentratedParticle.ManaConcentratedProvider::new);
    }
}
