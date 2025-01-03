package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.mana.IManaItem;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ManaCollectorBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class RunePedestalBlockEntity extends BlockEntity {
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            RunePedestalBlockEntity.this.markChanged();
        }
    };

    public RunePedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void markChanged() {
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(),
                                   ManaCollectorBlock.UPDATE_ALL);
            level.invalidateCapabilities(worldPosition);
        }
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return inventory;
    }

    public IManaHandler getManaHandler(@Nullable Direction side) {
        ItemStack stack = inventory.getStackInSlot(0);
        if (!stack.isEmpty() && stack.getItem() instanceof IManaItem) {
            return stack.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
        }
        return null;
    }

    public boolean placeItem(ItemStack stack) {
        if (inventory.getStackInSlot(0).isEmpty()) {
            inventory.setStackInSlot(0, stack.copy());
            stack.setCount(0);
            this.markChanged();
            return true;
        }
        return false;
    }

    public ItemStack removeItem() {
        ItemStack stack = inventory.getStackInSlot(0);
        inventory.setStackInSlot(0, ItemStack.EMPTY);
        this.markChanged();
        return stack;
    }

    public ItemStack getItem() {
        return inventory.getStackInSlot(0);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    public void setItem(ItemStack item) {
        inventory.setStackInSlot(0, item);
    }
}
