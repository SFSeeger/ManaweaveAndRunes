package io.github.sfseeger.lib.common.mana;

import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public interface IManaItem {
    int MANA_CAPACITY = 5000;
    int MAX_EXTRACT = 200;
    int MAX_RECEIVE = 400;

    default int getManaBarWidth(ItemStack stack) {
        IManaHandler manaHandler = getManaHandler(stack);
        return Math.round(
                (float) manaHandler.getManaStored(getManaType()) / (float) manaHandler.getManaCapacity() * 13.0f);
    }

    default int getManaBarColor(ItemStack stack) {
        return getManaType().properties.getColor();
    }

    default boolean isManaBarVisible(ItemStack stack) {
        IManaHandler manaHandler = getManaHandler(stack);
        return manaHandler != null && manaHandler.getManaStored(getManaType()) > 0 && manaHandler.getManaStored(
                getManaType()) < manaHandler.getManaCapacity();
    }

    default void addTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        IManaHandler manaHandler = getManaHandler(stack);
        if (manaHandler == null) return;
        MutableComponent component = Component.translatable("lore.manaweaveandrunes.mana_stored",
                                                            String.format("%,d",
                                                                          manaHandler.getManaStored(getManaType())),
                                                            String.format("%,d",
                                                                          manaHandler.getManaCapacity()))
                .append(" ")
                .append(getManaType().getName())
                .withColor(getManaType().properties.getColor());

        tooltip.add(component);

    }

    default int getManaCapacity() {
        return MANA_CAPACITY;
    }

    default int getMaxExtract() {
        return MAX_EXTRACT;
    }

    default int getMaxReceive() {
        return MAX_RECEIVE;
    }

    default IManaHandler getManaHandler(ItemStack stack) {
        return stack.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
    }

    Mana getManaType();
}
