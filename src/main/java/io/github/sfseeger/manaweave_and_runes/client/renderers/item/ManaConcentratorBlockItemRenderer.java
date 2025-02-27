package io.github.sfseeger.manaweave_and_runes.client.renderers.item;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.items.ManaConcentratorBlockItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ManaConcentratorBlockItemRenderer extends GeoItemRenderer<ManaConcentratorBlockItem> {
    public ManaConcentratorBlockItemRenderer() {
        super(new DefaultedBlockGeoModel<>(
                ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana_concentrator")));
    }
}
