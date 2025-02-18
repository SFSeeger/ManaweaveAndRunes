package io.github.sfseeger.manaweave_and_runes.common.menus;

import io.github.sfseeger.lib.common.spells.IUpgradable;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.WandModificationTableBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.menus.slots.SpellCircleSlot;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit.WAND_MODIFICATION_TABLE_MENU;

public class WandModificationTableMenu extends AbstractContainerMenu {
    private static final int INV_SLOT_START = 13;
    private static final int HOTBAR_SLOT_START = INV_SLOT_START + 27;
    private static final int HOTBAR_SLOT_END = HOTBAR_SLOT_START + 9;
    private final ContainerLevelAccess access;
    private final WandModificationTableBlockEntity blockEntity;
    private final ItemStackHandler itemHandler;
    private final SpellCircleSlot[] spellCircleSlots = new SpellCircleSlot[12];
    private IItemHandler upgradeableItemHandler;

    public WandModificationTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(containerId,
                playerInventory,
                (WandModificationTableBlockEntity) playerInventory.player.level().getBlockEntity(buffer.readBlockPos()),
                ContainerLevelAccess.NULL);
    }

    public WandModificationTableMenu(int containerId, Inventory playerInventory, WandModificationTableBlockEntity blockEntity, ContainerLevelAccess access) {
        super(WAND_MODIFICATION_TABLE_MENU.get(), containerId);
        this.access = access;
        this.blockEntity = blockEntity;
        this.itemHandler = blockEntity.getItemHandler(null);

        this.addSlot(new SlotItemHandler(itemHandler, 0, 16, 33) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                if (stack.getItem() instanceof IUpgradable) {
                    WandModificationTableMenu.this.upgradeableItemHandler = null;
                    for (int i = 0; i < 12; i++) {
                        spellCircleSlots[i].setItemHandler(null);
                    }
                }
                super.onTake(player, stack);
            }

            @Override
            public void setChanged() {
                if (this.getItem().isEmpty()) {
                    WandModificationTableMenu.this.upgradeableItemHandler = null;
                    for (int i = 0; i < 12; i++) {
                        spellCircleSlots[i].setItemHandler(null);
                    }
                }
                super.setChanged();
            }

            @Override
            public void set(ItemStack stack) {
                if (stack.getItem() instanceof IUpgradable) {
                    WandModificationTableMenu.this.upgradeableItemHandler =
                            stack.getCapability(Capabilities.ItemHandler.ITEM);
                    for (int i = 0; i < 12; i++) {
                        spellCircleSlots[i].setItemHandler(WandModificationTableMenu.this.upgradeableItemHandler);
                    }
                }
                super.set(stack);
            }
        });

        if (hasUpgradable()) {
            this.upgradeableItemHandler = this.blockEntity.getItemHandler(null)
                    .getStackInSlot(0)
                    .getCapability(Capabilities.ItemHandler.ITEM);
        }
        // Add spell circle slots
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                SpellCircleSlot slot = new SpellCircleSlot(upgradeableItemHandler, j + i * 4, 53 + j * 18, 15 + i * 18);
                spellCircleSlots[j + i * 4] = slot;
                this.addSlot(slot);
            }
        }

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

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(index);

        if (quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();
            // Does the item come from the player's inventory?
            if (index >= INV_SLOT_START && index < HOTBAR_SLOT_END) {
                boolean couldMove;
                // Is the item a upgradable?
                if (quickMovedStack.getItem() instanceof IUpgradable) {
                    couldMove = this.moveItemStackTo(rawStack, 0, 1, false);
                } else {
                    couldMove = this.moveItemStackTo(rawStack, 1, 13, false);
                }

                if (!couldMove) {
                    if (index < HOTBAR_SLOT_START) {
                        // Try to move the item to the player's hotbar
                        if (!this.moveItemStackTo(rawStack, HOTBAR_SLOT_START, HOTBAR_SLOT_END, false)) {
                            return ItemStack.EMPTY;
                        }
                        // Move to the player's inventory
                    } else if (!this.moveItemStackTo(rawStack, INV_SLOT_START, HOTBAR_SLOT_START, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Else if the quick move was performed on the data inventory input slots, try to move to player inventory/hotbar
            } else if (!this.moveItemStackTo(rawStack, INV_SLOT_START, HOTBAR_SLOT_END, false)) {
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
                player, ManaweaveAndRunesBlockInit.WAND_MODIFICATION_TABLE_BLOCK.get());
    }

    public boolean hasUpgradable() {
        return this.blockEntity.getItemHandler(null).getStackInSlot(0).getItem() instanceof IUpgradable;
    }

    public int getSlotAmount() {
        return upgradeableItemHandler == null ? 0 : this.upgradeableItemHandler.getSlots();
    }
}
