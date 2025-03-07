package io.github.sfseeger.lib.common.mana.capability;

import io.github.sfseeger.lib.common.mana.Mana;
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
        return getManaType().properties().getColor();
    }

    default boolean isManaBarVisible(ItemStack stack) {
        IManaHandler manaHandler = getManaHandler(stack);
        return manaHandler != null && manaHandler.getManaStored(getManaType()) > 0;
    }

    default void addTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        IManaHandler manaHandler = getManaHandler(stack);
        if (manaHandler == null) return;
        MutableComponent component = Component.translatable("lore.manaweave_and_runes.mana_stored",
                                                            String.format("%,d",
                                                                          manaHandler.getManaStored(getManaType())),
                                                            String.format("%,d",
                                                                          manaHandler.getManaCapacity()))
                .append(" ")
                .append(getManaType().getName())
                .withColor(getManaType().properties().getColor());

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

    /**
     * @return The type of mana this item can store or interact with. If this returns null, the mana stored in this item is not set.
     */
    Mana getManaType();

    /**
     * @return A list of mana types this item can store or interact with. Should read it from the actual ManaHandler if it has one.
     */
    default List<Mana> getManaTypes(ItemStack stack) {
        IManaHandler handler = stack.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
        if (handler == null) return List.of(getManaType());
        return handler.getManaTypesStored();
    }
}
