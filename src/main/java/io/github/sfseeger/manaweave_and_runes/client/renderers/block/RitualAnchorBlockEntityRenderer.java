package io.github.sfseeger.manaweave_and_runes.client.renderers.block;

import io.github.sfseeger.lib.client.block_entity_renderers.GeckoManaNodeRenderer;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RitualAnchorBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class RitualAnchorBlockEntityRenderer extends GeckoManaNodeRenderer<RitualAnchorBlockEntity> {
    public RitualAnchorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new DefaultedBlockGeoModel<>(
                ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "ritual_anchor")));
    }
}
