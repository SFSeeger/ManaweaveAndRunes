package io.github.sfseeger.manaweave_and_runes.client.event;

import io.github.sfseeger.lib.client.block_entity_renderers.ManaNodeRenderer;
import io.github.sfseeger.lib.client.entity_renderers.SpellProjectileRenderer;
import io.github.sfseeger.lib.client.models.SpellProjectileModel;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.client.particles.ManaConcentratedParticle;
import io.github.sfseeger.manaweave_and_runes.client.particles.ManaTravelParticle;
import io.github.sfseeger.manaweave_and_runes.client.particles.mana_particle.ManaParticleType;
import io.github.sfseeger.manaweave_and_runes.client.renderers.block.ManaCollectorBlockEntityRenderer;
import io.github.sfseeger.manaweave_and_runes.client.renderers.block.ManaConcentratorBlockEntityRenderer;
import io.github.sfseeger.manaweave_and_runes.client.renderers.block.RitualAnchorBlockEntityRenderer;
import io.github.sfseeger.manaweave_and_runes.client.renderers.block.RunePedestalBlockEntityRenderer;
import io.github.sfseeger.manaweave_and_runes.client.screens.*;
import io.github.sfseeger.manaweave_and_runes.core.init.EntityTypeInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRMenuInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRParticleTypeInit;
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
        event.registerBlockEntityRenderer(MRBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(),
                                          ManaCollectorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MRBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get(),
                                          RunePedestalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MRBlockEntityInit.MANA_CONCENTRATOR_BLOCK_ENTITY.get(),
                                          ManaConcentratorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MRBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(),
                                          ManaNodeRenderer::new);
        event.registerBlockEntityRenderer(MRBlockEntityInit.RITUAL_ANCHOR_BLOCK_ENTITY.get(),
                                          RitualAnchorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(MRBlockEntityInit.MANA_TRANSMITTER_BLOCK_ENTITY.get(),
                                          ManaNodeRenderer::new);
        event.registerBlockEntityRenderer(MRBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get(),
                                          ManaNodeRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SpellProjectileModel.LAYER_LOCATION, SpellProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeInit.SPELL_PROJECTILE.get(), SpellProjectileRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MRMenuInit.RUNE_CARVER_BLOCK_MENU.get(), RuneCarverBlockScreen::new);
        event.register(MRMenuInit.MANA_STORAGE_BLOCK_MENU.get(), ManaStorageBlockScreen::new);
        event.register(MRMenuInit.WAND_MODIFICATION_TABLE_MENU.get(), WandModificationTableScreen::new);
        event.register(MRMenuInit.SPELL_DESIGNER_MENU.get(), SpellDesignerScreen::new);
        event.register(MRMenuInit.MANA_GENERATOR_MENU.get(), ManaGeneratorScreen::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MRParticleTypeInit.MANA_TRAVEL_PARTICLE.get(),
                                ManaTravelParticle.ManaTravelParticleProvider::new);
        event.registerSpriteSet(MRParticleTypeInit.MANA_CONCENTRATED.get(),
                                ManaConcentratedParticle.ManaConcentratedProvider::new);
        event.registerSpriteSet(MRParticleTypeInit.MANA_PARTICLE.get(), ManaParticleType.Factory::new);
    }
}
