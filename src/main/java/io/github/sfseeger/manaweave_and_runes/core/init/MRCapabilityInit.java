package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.items.AbstractRuneItem;
import io.github.sfseeger.lib.common.items.IItemHandlerItem;
import io.github.sfseeger.lib.common.items.SpellHolderItem;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaItem;
import io.github.sfseeger.lib.common.mana.capability.ItemStackManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.lib.common.mana.capability.ProxyManaHandler;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MRCapabilityInit {
    private static void registerManaCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                MRBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get(),
                ManaGeneratorBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                MRBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(),
                ManaCollectorBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                MRBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get(),
                RunePedestalBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                MRBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(),
                ManaStorageBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                MRBlockEntityInit.RITUAL_ANCHOR_BLOCK_ENTITY.get(),
                RitualAnchorBlockEntity::getManaHandler
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
                MRItemInit.AMETHYST_FIRE_RUNE_ITEM.get(),
                MRItemInit.AMETHYST_AIR_RUNE_ITEM.get(),
                MRItemInit.AMETHYST_WATER_RUNE_ITEM.get(),
                MRItemInit.AMETHYST_EARTH_RUNE_ITEM.get(),
                MRItemInit.AMETHYST_VOID_RUNE_ITEM.get(),
                MRItemInit.AMETHYST_ORDER_RUNE_ITEM.get(),
                MRItemInit.AMETHYST_ENTROPY_RUNE_ITEM.get(),
                MRItemInit.AMETHYST_SOUL_RUNE_ITEM.get()
        );

        event.registerItem(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM,
                (itemstack, context) -> {
                    if (itemstack.getItem() instanceof IItemHandlerItem itemHandlerItem) {
                        return new ProxyManaHandler(itemHandlerItem.getItemHandler(itemstack));
                    }
                    return null;
                },
                MRItemInit.RUNE_BRACELET_ITEM.get()
        );
    }

    private static void registerItemHandlerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                MRBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(),
                ManaCollectorBlockEntity::getItemHandler
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                MRBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get(),
                RunePedestalBlockEntity::getItemHandler
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                MRBlockEntityInit.RUNEWROUGHT_BENCH_BLOCK_ENTITY.get(),
                RunewroughtBenchBlockEntity::getItemHandler
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                MRBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get(),
                ManaGeneratorBlockEntity::getItemHandler
        );

        // SpellHolderItemHandler
        event.registerItem(
                Capabilities.ItemHandler.ITEM,
                (itemstack, context) -> {
                    if (itemstack.getItem() instanceof IItemHandlerItem itemHandlerItem) {
                        return itemHandlerItem.getItemHandler(itemstack);
                    }
                    return new ItemStackHandler(1) {
                        @Override
                        public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) {
                            return stack.getItem() instanceof SpellHolderItem;
                        }

                        @Override
                        protected int getStackLimit(int slot, ItemStack stack) {
                            return 1;
                        }
                    };
                },

                MRItemInit.MANA_WEAVERS_STAFF_ITEM.get()
        );

        // RuneBraceletItemHandler
        event.registerItem(
                Capabilities.ItemHandler.ITEM,
                (itemstack, context) -> {
                    if (itemstack.getItem() instanceof IItemHandlerItem itemHandlerItem) {
                        return itemHandlerItem.getItemHandler(itemstack);
                    }
                    return new ItemStackHandler(1) {
                        @Override
                        protected int getStackLimit(int slot, ItemStack stack) {
                            return 1;
                        }

                        @Override
                        public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) {
                            return stack.getItem() instanceof AbstractRuneItem;
                        }
                    };
                },

                MRItemInit.RUNE_BRACELET_ITEM.get()
        );
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerManaCapabilities(event);
        registerItemHandlerCapabilities(event);
    }
}
