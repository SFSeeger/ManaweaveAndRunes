package io.github.sfseeger.manaweave_and_runes.common.menus;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaGeneratorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RunicLoomBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRMenuInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRTagInit;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RunicLoomMenu extends AbstractContainerMenu {
    private static final int CHISEL_SLOT = 0;
    private static final int RESULT_SLOT = 1;
    private static final int INVENTORY_START = RESULT_SLOT;
    private static final int INVENTORY_END = INVENTORY_START + 27;
    private static final int HOTBAR_START = INVENTORY_END;
    private static final int HOTBAR_END = HOTBAR_START + 9;

    public static final int RUNE_COLUMNS = 12;
    public static final int RUNE_ROWS = 10;

    private final ContainerLevelAccess access;
    private final RunicLoomBlockEntity blockEntity;
    private final ItemStackHandler itemHandler;
    private final Mana[][] runeSlots = new Mana[RUNE_ROWS][RUNE_COLUMNS];

    public RunicLoomMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(containerId, playerInventory,
             (RunicLoomBlockEntity) playerInventory.player.level().getBlockEntity(buffer.readBlockPos()),
             ContainerLevelAccess.NULL);
    }

    public RunicLoomMenu(int containerId, Inventory playerInventory, RunicLoomBlockEntity blockEntity, ContainerLevelAccess access) {
        super(MRMenuInit.RUNIC_LOOM_MENU.get(), containerId);
        this.access = access;
        this.blockEntity = blockEntity;
        this.itemHandler = blockEntity.getItemHandler(null);

        addSlot(new SlotItemHandler(itemHandler, 0, 17, 32));
        addSlot(new SlotItemHandler(itemHandler, 1, 17, 96));

        // Add player inventory slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 48 + j * 18, 140 + i * 18));
            }
        }

        // Add player hotbar slots
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 48 + k * 18, 198));
        }
    }

    public Mana[][] getRuneSlots(){
        return runeSlots;
    }
    public @Nullable Mana getRuneInSlot(int x, int y) {
        return runeSlots[y][x];
    }
    public void setRuneInSlot(int x, int y, Mana rune) {
        runeSlots[y][x] = rune;
    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(index);

        if (quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();
            // Does the item come from the player's inventory?
            if (index >= INVENTORY_START && index <= HOTBAR_END) {
                boolean couldMove = false;
                // Is the item chisel?
                if (rawStack.is(MRTagInit.CHISEL_ITEM)) {
                    couldMove = this.moveItemStackTo(rawStack, CHISEL_SLOT, CHISEL_SLOT + 1, false);
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
        return AbstractContainerMenu.stillValid(this.access, player, MRBlockInit.RUNIC_LOOM_BLOCK.get());
    }

    public boolean hasChisel() {
        return this.itemHandler.getStackInSlot(CHISEL_SLOT).is(MRTagInit.CHISEL_ITEM);
    }
}
