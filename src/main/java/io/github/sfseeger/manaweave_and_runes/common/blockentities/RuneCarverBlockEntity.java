package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipe;
import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipeInput;
import io.github.sfseeger.manaweave_and_runes.common.menus.RuneCarverBlockMenu;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RuneCarverBlockEntity extends BlockEntity {
    public static final int NUM_SLOTS = 3;
    public static final int CHISEL_SLOT = 0;
    public static final int RUNE_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    private final ItemStackHandler itemStackHandler;
    private final DataSlot selectedRuneIndex = new DataSlot() {
        int selectedRuneIndex = -1;

        @Override
        public int get() {
            return selectedRuneIndex;
        }

        @Override
        public void set(int i) {
            selectedRuneIndex = i;
            RuneCarverBlockEntity.this.markChanged();
        }
    };


    public RuneCarverBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.RUNE_CARVER_BLOCK_ENTITY.get(), pos, blockState);

        itemStackHandler = new ItemStackHandler(3);
    }

    public IItemHandler getItemStackHandler() {
        return itemStackHandler;
    }

    protected void markChanged() {
        setChanged();
        invalidateCapabilities();
    }

    public DataSlot selectedRuneIndex() {
        return selectedRuneIndex;
    }

    public void setSelectedRuneIndex(int index) {
        selectedRuneIndex.set(index);
        markChanged();
    }

    public RuneCarverRecipeInput getRecipeInput() {
        return new RuneCarverRecipeInput(itemStackHandler.getStackInSlot(CHISEL_SLOT),
                                         itemStackHandler.getStackInSlot(RUNE_SLOT));
    }

    public void craftRune(List<RecipeHolder<RuneCarverRecipe>> recipes) {
        if (recipes.isEmpty() || !(selectedRuneIndex.get() >= 0 || selectedRuneIndex.get() < recipes.size())) {
            return;
        }

        RuneCarverRecipe recipe = recipes.get(this.selectedRuneIndex.get()).value();
        ItemStack output = recipe.assemble(this.getRecipeInput(), this.level.registryAccess());
        this.itemStackHandler.getStackInSlot(RUNE_SLOT).shrink(1);
        this.itemStackHandler.setStackInSlot(OUTPUT_SLOT, output);
        System.out.println("Crafted rune: " + output);
        markChanged();
        System.out.println("Item in output slot: " + itemStackHandler.getStackInSlot(OUTPUT_SLOT));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemStackHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        selectedRuneIndex.set(tag.getInt("selectedRuneIndex"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemStackHandler.serializeNBT(registries));
        tag.putInt("selectedRuneIndex", selectedRuneIndex.get());
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

    public RuneCarverBlockMenu getMenu(int id, Inventory playerInventory, ContainerLevelAccess containerLevelAccess) {
        return new RuneCarverBlockMenu(id, playerInventory, itemStackHandler, selectedRuneIndex, this,
                                       containerLevelAccess);
    }
}
