package io.github.sfseeger.manaweave_and_runes.client.renderers.item;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.items.ManaWeaversStaffItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ManaWeaversStaffRenderer extends GeoItemRenderer<ManaWeaversStaffItem> {
    public ManaWeaversStaffRenderer() {
        super(new DefaultedItemGeoModel<>(
                ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana_weavers_staff")));
    }
}
