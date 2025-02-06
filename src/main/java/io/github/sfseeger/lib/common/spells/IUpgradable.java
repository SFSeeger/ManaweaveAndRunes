package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public interface IUpgradable {
    IItemHandler getItemHandler(ItemStack stack);

    int getSlotCount();
    
}
