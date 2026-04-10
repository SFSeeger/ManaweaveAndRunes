package io.github.sfseeger.manaweave_and_runes.core.util;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.IManaItem;
import io.github.sfseeger.lib.common.mana.capability.ProxyManaHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ManaUtils {
    public static Map<Mana, Integer> getManaAvailable(Player player) {
        Map<Mana, Integer> totalManaAvailable = new HashMap<>();
        player.getInventory().items.stream().filter(stack -> stack.getItem() instanceof IManaItem).forEach(stack -> {
            calculateItemStackManaAvailable(stack).forEach(
                    (mana, manaAvailable) -> totalManaAvailable.merge(mana, manaAvailable, Integer::sum));
        });
        calculateItemStackManaAvailable(player.getOffhandItem()).forEach(
                (mana, manaAvailable) -> totalManaAvailable.merge(mana, manaAvailable, Integer::sum));
        return totalManaAvailable;
    }

    private static Map<Mana, Integer> calculateItemStackManaAvailable(ItemStack stack) {
        if (!(stack.getItem() instanceof IManaItem manaItem)) return Map.of();
        IManaHandler manaHandler = manaItem.getManaHandler(stack);
        return manaHandler.getManaTypesStored()
                .stream()
                .collect(Collectors.toMap(mana -> mana, manaHandler::getManaStored));
    }


    public static Map<Mana, Integer> getManaTotal(Player player) {
        Map<Mana, Integer> manTotal = new HashMap<>();
        player.getInventory().items.stream().filter(stack -> stack.getItem() instanceof IManaItem).forEach(stack -> {
            calculateItemStackManaTotal(stack).forEach(
                    (mana, manaTotal) -> manTotal.merge(mana, manaTotal, Integer::sum));
        });
        calculateItemStackManaTotal(player.getOffhandItem()).forEach(
                (mana, manaTotal) -> manTotal.merge(mana, manaTotal, Integer::sum));
        return manTotal;
    }

    private static Map<Mana, Integer> calculateItemStackManaTotal(ItemStack stack) {
        if (!(stack.getItem() instanceof IManaItem manaItem)) return Map.of();
        IManaHandler manaHandler = manaItem.getManaHandler(stack);
        if (manaHandler instanceof ProxyManaHandler proxyManaHandler) {
            return manaHandler.getManaTypesStored()
                    .stream()
                    .collect(Collectors.toMap(mana -> mana, proxyManaHandler::getManaCapacity));
        } else {
            return manaHandler.getManaTypesStored()
                    .stream()
                    .collect(Collectors.toMap(mana -> mana, mana -> manaHandler.getManaCapacity()));
        }
    }
}
