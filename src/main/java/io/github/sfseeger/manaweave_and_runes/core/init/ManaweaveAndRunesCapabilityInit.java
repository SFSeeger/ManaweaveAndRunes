package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.mana.IManaItem;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.ItemStackManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaCollectorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaGeneratorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaStorageBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RunePedestalBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;
import java.util.function.Supplier;

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
                ManaweaveAndRunesBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get(),
                ManaGeneratorBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                ManaweaveAndRunesBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(),
                ManaStorageBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                ManaweaveAndRunesBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get(),
                RunePedestalBlockEntity::getManaHandler
        );

        event.registerItem(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM,
                (itemstack, context) -> {
                    int capacity = 3000;
                    int maxExtract = 200;
                    int maxInsert = 400;
                    List<Supplier<Mana>> allowedMana = null;
                    if (itemstack.getItem() instanceof IManaItem manaItem) {
                        capacity = manaItem.getManaCapacity();
                        maxExtract = manaItem.getMaxExtract();
                        maxInsert = manaItem.getMaxReceive();
                        allowedMana = List.of(manaItem::getManaType);
                    }
                    return new ItemStackManaHandler(itemstack, capacity, maxExtract, maxInsert,
                                                    allowedMana);
                },
                ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM.get(),
                ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM.get()
        );
    }

    private static void registerItemHandlerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ManaweaveAndRunesBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(),
                ManaCollectorBlockEntity::getItemHandler
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ManaweaveAndRunesBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get(),
                RunePedestalBlockEntity::getItemHandler
        );
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerManaCapabilities(event);
        registerItemHandlerCapabilities(event);
    }
}
