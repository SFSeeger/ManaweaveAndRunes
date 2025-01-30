package io.github.sfseeger.manaweave_and_runes.common.menus;

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

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit.DIAMOND_CHISEL;
import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit.SPELL_DESIGNER_MENU;

public class SpellDesignerMenu extends AbstractContainerMenu {
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
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access,
                player, ManaweaveAndRunesBlockInit.SPELL_DESIGNER_BLOCK.get());
    }

    public boolean hasChisel() {
        ItemStack s = itemHandler.getStackInSlot(5);
        return !s.isEmpty() && s.getItem() == DIAMOND_CHISEL.get(); //TODO: Change to chisel item
    }

    public boolean setName(String name) {
        if (name.length() > 50) {
            return false;
        }
        blockEntity.setSpellName(name);
        return true;
    }

    public String getName() {
        return blockEntity.getSpellName();
    }

    public void craft(LocalPlayer player, String name) {
        PacketDistributor.sendToServer(new CraftPayload(blockEntity.getBlockPos().asLong(), 0, name));
    }
}
