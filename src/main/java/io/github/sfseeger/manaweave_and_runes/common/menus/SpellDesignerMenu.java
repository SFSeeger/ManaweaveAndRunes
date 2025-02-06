package io.github.sfseeger.manaweave_and_runes.common.menus;

import io.github.sfseeger.lib.common.items.SpellPartHolderItem;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.SpellPart;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.SpellDesignerBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.payloads.CraftPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit.SPELL_PART_DATA_COMPONENT;
import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit.DIAMOND_CHISEL;
import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit.SPELL_DESIGNER_MENU;

public class SpellDesignerMenu extends AbstractContainerMenu {
    private static final int INV_SLOT_START = 6;
    private static final int HOTBAR_SLOT_START = INV_SLOT_START + 27;
    private static final int HOTBAR_SLOT_END = HOTBAR_SLOT_START + 9;
    private final ContainerLevelAccess access;
    private final SpellDesignerBlockEntity blockEntity;
    private final ItemStackHandler itemHandler;

    public SpellDesignerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(containerId,
                playerInventory,
                (SpellDesignerBlockEntity) playerInventory.player.level().getBlockEntity(buffer.readBlockPos()),
                ContainerLevelAccess.NULL);
    }

    public SpellDesignerMenu(int containerId, Inventory playerInventory, SpellDesignerBlockEntity blockEntity, ContainerLevelAccess access) {
        super(SPELL_DESIGNER_MENU.get(), containerId);
        this.access = access;
        this.blockEntity = blockEntity;
        this.itemHandler = blockEntity.getItemHandler();

        addSlot(new SlotItemHandler(itemHandler, 0, 81, 64));

        addSlot(new SlotItemHandler(itemHandler, 1, 80, 20));
        addSlot(new SlotItemHandler(itemHandler, 3, 80, 109));

        addSlot(new SlotItemHandler(itemHandler, 4, 36, 64));
        addSlot(new SlotItemHandler(itemHandler, 2, 125, 64));


        addSlot(new SlotItemHandler(itemHandler, 5, 204, 90));

        addSlot(new SlotItemHandler(itemHandler, 6, 223, 90));

        // Add player inventory slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }

        // Add player hotbar slots
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 198));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(index);

        if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();
            // Does the item come from the player's inventory?
            if (index >= INV_SLOT_START && index < HOTBAR_SLOT_END + 1) {
                boolean couldMove = false;
                // Is the item a chisel?
                if (quickMovedStack.getItem() == DIAMOND_CHISEL.get()) { //TODO: Replace wth chisel item
                    couldMove = this.moveItemStackTo(rawStack, 5, 6, false);
                // Is the item a spell Part?
                } else if (quickMovedStack.getItem() instanceof SpellPartHolderItem) {
                    SpellPart part = quickMovedStack.get(SPELL_PART_DATA_COMPONENT);
                    if(part != null && part.getCore() != null && part.getCore().value() instanceof AbstractSpellModifier) {
                        couldMove = this.moveItemStackTo(rawStack, 1, 5, false);
                    } else {
                        couldMove = this.moveItemStackTo(rawStack, 0, 5, false);
                    }
                    couldMove = this.moveItemStackTo(rawStack, 1, 13, false);
                }
                if (!couldMove) {
                    if (index < HOTBAR_SLOT_START) {
                        // Try to move the item to the player's hotbar
                        if (!this.moveItemStackTo(rawStack, HOTBAR_SLOT_START, HOTBAR_SLOT_END + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                        // Move to the player's inventory
                    } else if (!this.moveItemStackTo(rawStack, INV_SLOT_START, HOTBAR_SLOT_START, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Else if the quick move was performed on the data inventory input slots, try to move to player inventory/hotbar
            } else if (!this.moveItemStackTo(rawStack, INV_SLOT_START, HOTBAR_SLOT_END + 1, false)) {
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
                player, ManaweaveAndRunesBlockInit.SPELL_DESIGNER_BLOCK.get());
    }

    public boolean hasChisel() {
        return blockEntity.hasChisel();
    }

    public boolean setName(String name) {
        if (name.length() > 50) {
            return false;
        }
        blockEntity.setSpellName(name);
        return true;
    }

    public int getCooldown(){
        int result = 0;
        for(int i = 0; i < INV_SLOT_START; i++){
            ItemStack stack = this.slots.get(i).getItem();
            if(stack.getItem() instanceof SpellPartHolderItem){
                result += stack.get(SPELL_PART_DATA_COMPONENT).getCooldown();
            }
        }
        return result;
    }

    public String getName() {
        return blockEntity.getSpellName();
    }

    public void craft(LocalPlayer player, String name) {
        PacketDistributor.sendToServer(new CraftPayload(blockEntity.getBlockPos().asLong(), 0, name));
    }

    public Map<Mana, Integer> getManaCost() {
        return blockEntity.getManaCost();
    }
}
