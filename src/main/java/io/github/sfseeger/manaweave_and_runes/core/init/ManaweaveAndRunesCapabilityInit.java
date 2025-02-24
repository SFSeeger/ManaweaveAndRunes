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
public class ManaweaveAndRunesCapabilityInit {
    private static void registerManaCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                ManaweaveAndRunesBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get(),
                ManaGeneratorBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                ManaweaveAndRunesBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(),
                ManaCollectorBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                ManaweaveAndRunesBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get(),
                RunePedestalBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                ManaweaveAndRunesBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(),
                ManaStorageBlockEntity::getManaHandler
        );
        event.registerBlockEntity(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK,
                ManaweaveAndRunesBlockEntityInit.RITUAL_ANCHOR_BLOCK_ENTITY.get(),
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
                ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM.get(),
                ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM.get()
        );

        event.registerItem(
                ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM,
                (itemstack, context) -> {
                    if (itemstack.getItem() instanceof IItemHandlerItem itemHandlerItem) {
                        return new ProxyManaHandler(itemHandlerItem.getItemHandler(itemstack));
                    }
                    return null;
                },
                ManaweaveAndRunesItemInit.RUNE_BRACELET_ITEM.get()
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
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ManaweaveAndRunesBlockEntityInit.WAND_MODIFICATION_TABLE_BLOCK_ENTITY.get(),
                WandModificationTableBlockEntity::getItemHandler
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ManaweaveAndRunesBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get(),
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

                ManaweaveAndRunesItemInit.MANA_WEAVERS_STAFF_ITEM.get()
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

                ManaweaveAndRunesItemInit.RUNE_BRACELET_ITEM.get()
        );
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerManaCapabilities(event);
        registerItemHandlerCapabilities(event);
    }
}
