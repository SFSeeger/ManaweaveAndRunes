package io.github.sfseeger.manaweave_and_runes.common.menus;

import io.github.sfseeger.manaweave_and_runes.common.blockentities.WandModificationTableBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.Optional;

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit.WAND_MODIFICARION_TABLE_MENU;

public class WandModificationTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final WandModificationTableBlockEntity blockEntity;
    private final ItemStackHandler itemHandler;

    public WandModificationTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(containerId,
                playerInventory,
                (WandModificationTableBlockEntity) playerInventory.player.level().getBlockEntity(buffer.readBlockPos()),
                ContainerLevelAccess.NULL);
    }

    public WandModificationTableMenu(int containerId, Inventory playerInventory, WandModificationTableBlockEntity blockEntity, ContainerLevelAccess access) {
        super(WAND_MODIFICARION_TABLE_MENU.get(), containerId);
        this.access = access;
        this.blockEntity = blockEntity;
        this.itemHandler = blockEntity.getItemHandler(null);

        this.addSlot(new SlotItemHandler(itemHandler, 0, 0, 0));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY; //TODO: Add quick move logic
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access,
                player, ManaweaveAndRunesBlockInit.WAND_MODIFICATION_TABLE_BLOCK.get());
    }

    public Optional<IItemHandler> getWandSlots(){
        ItemStack wand =  itemHandler.getStackInSlot(0);
        return Optional.ofNullable(wand.getCapability(Capabilities.ItemHandler.ITEM));
    }
}
