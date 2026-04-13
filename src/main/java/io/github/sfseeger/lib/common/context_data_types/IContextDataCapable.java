package io.github.sfseeger.lib.common.context_data_types;

import net.minecraft.world.item.ItemStack;

public interface IContextDataCapable {
    <T extends IContextDataType> T getData(ItemStack stack);
}
