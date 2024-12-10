package io.github.sfseeger.manaweave_and_runes.core.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InventoryUtil {
    public static void shrinkStackIfSurvival(Player player, ItemStack stack, int amount) {
        if (!player.isCreative()) {
            stack.shrink(amount);
        }
    }
}
