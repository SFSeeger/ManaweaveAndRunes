package io.github.sfseeger.lib.common.items;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public interface IItemHandlerItem {
    IItemHandler getItemHandler(ItemStack stack);
    int getSlotCount();
}
