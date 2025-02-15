package io.github.sfseeger.manaweave_and_runes.common.menus;

import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaGeneratorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit.MANA_GENERATOR_MENU;

public class ManaGeneratorMenu extends AbstractContainerMenu {

    private static final int ITEM_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int INVENTORY_START = FUEL_SLOT;
    private static final int INVENTORY_END = INVENTORY_START + 27;
    private static final int HOTBAR_START = INVENTORY_END;
    private static final int HOTBAR_END = HOTBAR_START + 9;

    private final ContainerLevelAccess access;
    private final ManaGeneratorBlockEntity blockEntity;
    private final ItemStackHandler itemHandler;

    public ManaGeneratorMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(containerId, playerInventory,
             (ManaGeneratorBlockEntity) playerInventory.player.level().getBlockEntity(buffer.readBlockPos()),
             ContainerLevelAccess.NULL);
    }

    public ManaGeneratorMenu(int containerId, Inventory playerInventory, ManaGeneratorBlockEntity blockEntity,
            ContainerLevelAccess access) {
        super(MANA_GENERATOR_MENU.get(), containerId);
        this.access = access;
        this.blockEntity = blockEntity;
        this.itemHandler = blockEntity.getItemHandler(null);

        addSlot(new SlotItemHandler(itemHandler, 0, 80, 17));
        addSlot(new SlotItemHandler(itemHandler, 1, 80, 53));

        // Add player inventory slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Add player hotbar slots
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public float getCookProgress() {
        int cookTime = this.blockEntity.getCookTime();
        return cookTime != 0 ? (float) cookTime / (float) this.blockEntity.getMaxCookTime() : 0.0F;
    }

    public float getBurnProgress() {
        int remaining = this.blockEntity.getBurnTimeRemaining();
        int maxBurnTime = this.blockEntity.getMaxBurnTime();
        return maxBurnTime != 0 && remaining != 0 ? Mth.clamp((float) remaining / (float) maxBurnTime, 0.0F,
                                                              1.0F) : 0.0F;
    }

    public boolean isLit() {
        return this.blockEntity.isLit();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(index);

        if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();
            // Does the item come from the player's inventory?
            if (index >= INVENTORY_START && index <= HOTBAR_END) {
                boolean couldMove = false;
                // Is the item fuel?
                if (quickMovedStack.getBurnTime(null) > 0) {
                    couldMove = this.moveItemStackTo(rawStack, FUEL_SLOT, FUEL_SLOT + 12, false);
                    // Is the item mana holder?
                } else if (ManaGeneratorBlockEntity.getManaMapData(rawStack).isPresent()) {
                    couldMove = this.moveItemStackTo(rawStack, ITEM_SLOT, ITEM_SLOT + 1, false);
                }
                if (!couldMove) {
                    if (index < HOTBAR_START) {
                        // Try to move the item to the player's hotbar
                        if (!this.moveItemStackTo(rawStack, HOTBAR_START, HOTBAR_END + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                        // Move to the player's inventory
                    } else if (!this.moveItemStackTo(rawStack, INVENTORY_START, HOTBAR_START, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Else if the quick move was performed on the data inventory input slots, try to move to player inventory/hotbar
            } else if (!this.moveItemStackTo(rawStack, INVENTORY_START, HOTBAR_END + 1, false)) {
                return ItemStack.EMPTY;
            }

            if (rawStack.isEmpty()) {
                quickMovedSlot.set(ItemStack.EMPTY);
            } else {
                quickMovedSlot.setChanged();
            }

            if (rawStack.getCount() == quickMovedStack.getCount()) {
                return ItemStack.EMPTY;
            }
            quickMovedSlot.onTake(player, rawStack);
        }

        return quickMovedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access,
                                                player, ManaweaveAndRunesBlockInit.MANA_GENERATOR_BLOCK.get());
    }
}
