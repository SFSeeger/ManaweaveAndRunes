package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MRItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(MRItemInit.SOUL_CONTAINER_RUNE_ITEM.get(),
                                ResourceLocation.fromNamespaceAndPath(
                                        ManaweaveAndRunes.MODID, "contains_soul"),
                                (stack, level, entity, seed) -> stack.get(
                                        MRDataComponentsInit.PLAYER_DATA_COMPONENT) != null ? 1.0F : 0.0F);
        ItemProperties.register(MRItemInit.POSITION_RUNE_ITEM.get(),
                                ResourceLocation.fromNamespaceAndPath(
                                        ManaweaveAndRunes.MODID, "has_position"),
                                (stack, level, entity, seed) -> stack.get(
                                        MRDataComponentsInit.BLOCK_POS_DATA_COMPONENT) != null ? 1.0F : 0.0F);
    }

    @SubscribeEvent
    public static void registerItemProperties(FMLClientSetupEvent event) {
        addCustomItemProperties();
    }
}
