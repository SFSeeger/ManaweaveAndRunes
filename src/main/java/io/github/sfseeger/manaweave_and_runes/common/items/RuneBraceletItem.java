package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.items.AbstractRuneItem;
import io.github.sfseeger.lib.common.items.IItemHandlerItem;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.IManaItem;
import io.github.sfseeger.lib.common.mana.capability.ProxyManaHandler;
import io.github.sfseeger.lib.common.spells.IUpgradable;
import io.github.sfseeger.manaweave_and_runes.common.data_components.ItemStackHandlerDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class RuneBraceletItem extends Item implements IItemHandlerItem, IUpgradable, IManaItem {

    public RuneBraceletItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public IItemHandler getItemHandler(ItemStack stack) {
        if (!stack.has(ManaweaveAndRunesDataComponentsInit.ITEM_STACK_HANDLER_DATA_COMPONENT)) {
            ItemStackHandler handler = new ItemStackHandler(getSlotCount()) {
                @Override
                protected int getStackLimit(int slot, ItemStack stack) {
                    return 1;
                }

                @Override
                public boolean isItemValid(int slot, ItemStack stack) {
                    return stack.getItem() instanceof AbstractRuneItem;
                }
            };

            stack.set(ManaweaveAndRunesDataComponentsInit.ITEM_STACK_HANDLER_DATA_COMPONENT.get(),
                      new ItemStackHandlerDataComponent(handler));
        }

        return stack.get(ManaweaveAndRunesDataComponentsInit.ITEM_STACK_HANDLER_DATA_COMPONENT).getItemHandler();
    }

    @Override
    public int getSlotCount() {
        return 8;
    }

    public IManaHandler getManaHandler(ItemStack stack) {
        return new ProxyManaHandler(getItemHandler(stack));
    }

    @Override
    public Mana getManaType() {
        return null;
    }

    @Override
    public List<Mana> getManaTypes(ItemStack stack) {
        List<Mana> list = new ArrayList<>();
        for (int i = 0; i < getSlotCount(); i++) {
            ItemStack runeStack = getItemHandler(stack).getStackInSlot(i);
            if (runeStack.getItem() instanceof AbstractRuneItem runeItem) {
                list.add(runeItem.getManaType());
            }
        }
        return list;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        IManaHandler manaHandler = getManaHandler(stack);
        if (manaHandler == null) return;
        for (Mana mana : manaHandler.getManaTypesStored()) {
            int capacity;
            if (manaHandler instanceof ProxyManaHandler) {
                capacity = ((ProxyManaHandler) manaHandler).getManaCapacity(mana);
            } else {
                capacity = manaHandler.getManaCapacity();
            }
            MutableComponent component = Component.translatable("lore.manaweave_and_runes.mana_stored",
                                                                String.format("%,d",
                                                                              manaHandler.getManaStored(mana)),
                                                                String.format("%,d", capacity))
                    .append(" ")
                    .append(mana.getName())
                    .withColor(mana.properties().getColor());

            tooltipComponents.add(component);
        }
    }
}
