package io.github.sfseeger.manaweave_and_runes.common.menus.slots;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

public class SpellCircleSlot extends Slot {
    private static Container emptyInventory = new SimpleContainer(0);
    protected int index;
    private IItemHandler itemHandler;

    public SpellCircleSlot(@Nullable IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(emptyInventory, index, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.index = index;
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.isEmpty() ? false : hasItemHandler() && index < itemHandler.getSlots() && this.itemHandler.isItemValid(
                this.index, stack);
    }

    public ItemStack getItem() {
        if (!hasItemHandler() || this.index >= this.getItemHandler().getSlots()) {
            return ItemStack.EMPTY;
        }
        return this.getItemHandler().getStackInSlot(this.index);
    }

    public void set(ItemStack stack) {
        if (!hasItemHandler() || this.index >= this.getItemHandler().getSlots()) {
            return;
        }
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(this.index, stack);
        this.setChanged();
    }

    public ItemStack remove(int amount) {
        if (!hasItemHandler()) {
            return ItemStack.EMPTY;
        }
        return this.getItemHandler().extractItem(this.index, amount, false);
    }

    public boolean mayPickup(Player playerIn) {
        return hasItemHandler() && index < itemHandler.getSlots() && !this.getItemHandler()
                .extractItem(this.index, 1, true)
                .isEmpty();
    }

    public void initialize(ItemStack stack) {
        if (!hasItemHandler()) {
            return;
        }
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(this.index, stack);
        this.setChanged();
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public void setItemHandler(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    public boolean hasItemHandler() {
        return itemHandler != null;
    }

}
