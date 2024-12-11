package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.items.AbstractRuneItem;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaCollectorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaStorageBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ManaweaveAndRunesCapabilityInit {
    private static void registerManaCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                ManaweaveAndRunesBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(),
                ManaCollectorBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                ManaweaveAndRunesBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(),
                ManaStorageBlockEntity::getManaHandler
        );

        event.registerItem(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM,
                (itemstack, context) -> itemstack.getItem() instanceof AbstractRuneItem runeItem ? runeItem.getManaHandler() : null,
                ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM.get(),
                ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM.get()
        );
    }

    private static void registerItemCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ManaweaveAndRunesBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(),
                ManaCollectorBlockEntity::getItemHandler
        );
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerManaCapabilities(event);
        registerItemCapabilities(event);
    }
}
