package io.github.sfseeger.lib.common.mana.capability;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;

public class ManaweaveAndRunesCapabilities {
    public static final BlockCapability<IManaHandler, @Nullable Direction> MANA_HANDLER_BLOCK = BlockCapability.createSided(
            ResourceLocation.fromNamespaceAndPath("manaweave_and_runes", "mana_handler"),
            IManaHandler.class
    );
    public static final ItemCapability<IManaHandler, Void> MANA_HANDLER_ITEM = ItemCapability.create(
            ResourceLocation.fromNamespaceAndPath("manaweave_and_runes", "mana_handler"),
            IManaHandler.class,
            Void.class
    );
}
