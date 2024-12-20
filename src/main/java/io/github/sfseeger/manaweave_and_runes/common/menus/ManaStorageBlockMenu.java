package io.github.sfseeger.manaweave_and_runes.common.menus;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaStorageBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;

public class ManaStorageBlockMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final ManaStorageBlockEntity blockEntity;
    private final IItemHandler itemHandler;

    public ManaStorageBlockMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(containerId, playerInventory,
             (ManaStorageBlockEntity) playerInventory.player.level().getBlockEntity(buffer.readBlockPos()),
             ContainerLevelAccess.NULL);
    }

    public ManaStorageBlockMenu(int containerId, Inventory playerInventory, ManaStorageBlockEntity blockEntity,
            ContainerLevelAccess access) {
        super(ManaweaverAndRunesMenuInit.MANA_STORAGE_BLOCK_MENU.get(), containerId);
        this.access = access;
        this.blockEntity = blockEntity;
        itemHandler = new ItemStackHandler(ManaStorageBlockEntity.MANA_SLOTS);

        // Add mana slots
        for (int i = 0; i < ManaStorageBlockEntity.MANA_SLOTS; ++i) {
            this.addSlot(new SlotItemHandler(itemHandler, i, 44 + i * 18, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }

                @Override
                public boolean mayPickup(Player playerIn) {
                    return false;
                }
            });
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
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player,
                                                ManaweaveAndRunesBlockInit.MANA_STORAGE_BLOCK.get());
    }

    public Mana getManaInSlot(int index) {
        List<Mana> manaList = blockEntity.getManaHandler(null).getManaTypesStored();
        if (index < manaList.size()) {
            return manaList.get(index);
        }
        return null;
    }

    public int getManaStored(Mana mana) {
        return blockEntity.getManaHandler(null).getManaStored(mana);
    }

    public int getManaCapacity() {
        return blockEntity.getManaHandler(null).getManaCapacity();
    }
}
