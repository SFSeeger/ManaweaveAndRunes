package io.github.sfseeger.manaweave_and_runes.common.menus;

import com.google.common.collect.Lists;
import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipe;
import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipeInput;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesRecipeInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;

public class RuneCarverBlockMenu extends AbstractContainerMenu {
    public static final int CHISEL_SLOT = 0;
    public static final int RUNE_SLOT = 1;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = INV_SLOT_START + 26;
    private static final int HOTBAR_SLOT_START = INV_SLOT_END + 1;
    private static final int HOTBAR_SLOT_END = HOTBAR_SLOT_START + 8;
    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex;
    private final Level level;
    private List<RecipeHolder<RuneCarverRecipe>> recipes;
    private ItemStack input;
    private Runnable slotUpdateListener;
    private long lastSoundTime;


    public RuneCarverBlockMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new ItemStackHandler(2), ContainerLevelAccess.NULL);
    }

    public RuneCarverBlockMenu(int containerId, Inventory playerInventory, IItemHandler handler,
            ContainerLevelAccess access) {
        super(ManaweaverAndRunesMenuInit.RUNE_CARVER_BLOCK_MENU.get(), containerId);
        this.selectedRecipeIndex = DataSlot.standalone();
        this.recipes = Lists.newArrayList();
        this.input = ItemStack.EMPTY;
        this.access = access;
        this.level = playerInventory.player.level();
        this.slotUpdateListener = () -> {
        };

        this.addSlot(new SlotItemHandler(handler, CHISEL_SLOT, 20, 15) {
            @Override
            public void setChanged() {
                super.setChanged();
                RuneCarverBlockMenu.this.onSlotUpdate(this.index);
                RuneCarverBlockMenu.this.slotsChanged(this.container);
                RuneCarverBlockMenu.this.slotUpdateListener.run();
            }
        });
        this.addSlot(new SlotItemHandler(handler, RUNE_SLOT, 20, 53) {
            @Override
            public void setChanged() {
                super.setChanged();
                RuneCarverBlockMenu.this.onSlotUpdate(this.index);
                RuneCarverBlockMenu.this.slotsChanged(this.container);
                RuneCarverBlockMenu.this.slotUpdateListener.run();
            }
        });

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

        this.addDataSlot(this.selectedRecipeIndex);
        this.selectedRecipeIndex.set(-1);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public void updateSelectedRecipeIndex(int index) {
        this.selectedRecipeIndex.set(index);
    }

    private void onSlotUpdate(int slotIndex) {
        if (slotIndex == CHISEL_SLOT || slotIndex == RUNE_SLOT) {
            this.createRecipeList();
            broadcastChanges();
        }
    }

    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidRecipeIndex(id)) {
            this.updateSelectedRecipeIndex(id);
        }

        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(index);

        if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();

            if (index == RUNE_SLOT) {
                if (!this.moveItemStackTo(rawStack, INV_SLOT_START, HOTBAR_SLOT_END + 1, true)) {
                    return ItemStack.EMPTY;
                }
            }
            // Does the item come from the player's inventory?
            else if (index >= INV_SLOT_START && index < HOTBAR_SLOT_END + 1) {
                boolean couldMove = false;
                // TODO: replace with check for chisel item
                if (quickMovedStack.is(ManaweaveAndRunesItemInit.DIAMOND_CHISEL)) {
                    couldMove = this.moveItemStackTo(rawStack, CHISEL_SLOT, CHISEL_SLOT + 1, false);
                } else if (quickMovedStack.is(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE)) {
                    couldMove = this.moveItemStackTo(rawStack, RUNE_SLOT, RUNE_SLOT + 1, false);
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
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            this.clearContainer(player, this.slots.get(CHISEL_SLOT).container);
            this.clearContainer(player, this.slots.get(RUNE_SLOT).container);
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player,
                                                ManaweaveAndRunesBlockInit.RUNE_CARVER_BLOCK.get());
    }

    private boolean isValidRecipeIndex(int id) {
        return id >= 0 && id < this.recipes.size();
    }

    public void createRecipeList() {
        this.recipes =
                this.level.getRecipeManager()
                        .getRecipesFor(ManaweaveAndRunesRecipeInit.RUNE_CARVER_RECIPE_TYPE.get(), this.getRecipeInput(),
                                       this.level);
    }

    private RuneCarverRecipeInput getRecipeInput() {
        return new RuneCarverRecipeInput(getChiselSlot().getItem(), getRuneSlot().getItem());
    }

    public void registerUpdateListener(Runnable listener) {
        this.slotUpdateListener = listener;
    }

    public boolean hasInputItem() {
        return getChiselSlot().hasItem() && getRuneSlot().hasItem();
    }

    public List<RecipeHolder<RuneCarverRecipe>> getRecipes() {
        return this.recipes;
    }

    public Slot getChiselSlot() {
        return this.slots.get(CHISEL_SLOT);
    }

    public Slot getRuneSlot() {
        return this.slots.get(RUNE_SLOT);
    }

    public void assembleRune() {
        if (this.isValidRecipeIndex(this.getSelectedRecipeIndex())) {
            RuneCarverRecipe recipe = this.recipes.get(this.getSelectedRecipeIndex()).value();
            ItemStack result = recipe.assemble(this.getRecipeInput(), this.level.registryAccess());
            if (result.isItemEnabled(this.level.enabledFeatures())) {
                this.getRuneSlot().set(result);
                access.execute((level, blockPos) -> {
                    long l = level.getGameTime();
                    if (this.lastSoundTime != l) {
                        level.playSound(null, blockPos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS,
                                        1.0F, 1.0F);
                        this.lastSoundTime = l;
                    }
                });
            } else {
                this.getRuneSlot().set(ItemStack.EMPTY);
            }
            this.slotsChanged(this.getRuneSlot().container);
            this.broadcastChanges();
        }
    }

}