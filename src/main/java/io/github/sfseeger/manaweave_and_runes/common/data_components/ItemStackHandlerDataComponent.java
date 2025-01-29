package io.github.sfseeger.manaweave_and_runes.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemStackHandlerDataComponent {
    public static final Codec<ItemEntry> SLOT_ITEM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("slot").forGetter(ItemEntry::slot),
            ItemStack.CODEC.fieldOf("item").forGetter(ItemEntry::item)
    ).apply(instance, ItemEntry::new));

    public static final Codec<ItemStackHandlerDataComponent> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("size").forGetter(ItemStackHandlerDataComponent::getSlotCount),
                    SLOT_ITEM_CODEC.listOf().fieldOf("items").forGetter(ItemStackHandlerDataComponent::getItems)
            ).apply(instance, ItemStackHandlerDataComponent::new));

    public final static StreamCodec<RegistryFriendlyByteBuf, ItemEntry> ITEM_STACK_LIST_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ItemEntry::slot,
            ItemStack.STREAM_CODEC, ItemEntry::item,
            ItemEntry::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackHandlerDataComponent> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, ItemStackHandlerDataComponent::getSlotCount,
                    ITEM_STACK_LIST_CODEC.apply(ByteBufCodecs.list()), ItemStackHandlerDataComponent::getItems,
                    ItemStackHandlerDataComponent::new
            );

    private final IItemHandler itemHandler;

    public ItemStackHandlerDataComponent(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    public ItemStackHandlerDataComponent(int size, List<ItemEntry> items) {
        this.itemHandler = new ItemStackHandler(size);
        for (ItemEntry entry : items) {
            itemHandler.insertItem(entry.slot(), entry.item(), false);
        }
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public int getSlotCount() {
        return itemHandler.getSlots();
    }

    public List<ItemEntry> getItems() {
        List<ItemEntry> items = new ArrayList<>();
        for (int i = 0; i < getSlotCount(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                items.add(new ItemEntry(i, stack));
            }
        }
        return items;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itemHandler);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemStackHandlerDataComponent that)) return false;
        return Objects.equals(itemHandler, that.itemHandler);
    }

    public static record ItemEntry(int slot, ItemStack item) {
    }
}
