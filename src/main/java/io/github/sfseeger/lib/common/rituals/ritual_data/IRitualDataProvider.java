package io.github.sfseeger.lib.common.rituals.ritual_data;

import net.minecraft.world.item.ItemStack;

public interface IRitualDataProvider {
    <T extends IRitualData> T getData(ItemStack stack);
}
