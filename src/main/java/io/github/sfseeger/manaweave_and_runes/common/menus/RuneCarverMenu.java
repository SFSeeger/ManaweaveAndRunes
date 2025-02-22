package io.github.sfseeger.manaweave_and_runes.common.menus;

import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipe;
import io.github.sfseeger.lib.common.recipes.rune_carver.RuneCarverRecipeInput;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesRecipeInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaverAndRunesMenuInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.OptionalInt;

public class RuneCarverMenu extends ItemCombinerMenu {
    public static final int CHISEL_SLOT = 0;
    public static final int BASE_SLOT = 2;
    public static final int TEMPLATE_SLOT = 1;
    public static final int RESULT_SLOT = 3;
    private final Level level;

    @Nullable
    private RecipeHolder<RuneCarverRecipe> selectedRecipe;
    private final List<RecipeHolder<RuneCarverRecipe>> recipes;

    public RuneCarverMenu(int containerId, Inventory playerInventory){
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public RuneCarverMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ManaweaverAndRunesMenuInit.RUNE_CARVER_BLOCK_MENU.get(), containerId, playerInventory, access);
        this.level = playerInventory.player.level();
        this.recipes = this.level.getRecipeManager().getAllRecipesFor(ManaweaveAndRunesRecipeInit.RUNE_CARVER_RECIPE_TYPE.get());
    }

    private static OptionalInt findSlotMatchingIngredient(RuneCarverRecipe recipe, ItemStack stack) {
        if (recipe.isChiselIngredient(stack)) {
            return OptionalInt.of(0);
        } else if (recipe.isTemplateIngredient(stack)) {
            return OptionalInt.of(1);
        } else {
            return recipe.isBaseIngredient(stack) ? OptionalInt.of(2) : OptionalInt.empty();
        }
    }

    @Override
    protected boolean mayPickup(Player player, boolean b) {
        return this.selectedRecipe != null && this.selectedRecipe.value().matches(this.createRecipeInput(), this.level);
    }

    private void damageStackInSlot(int index){
        ItemStack itemStack = this.inputSlots.getItem(index);
        if(!itemStack.isEmpty() && !level.isClientSide){
            itemStack.hurtAndBreak(1, (ServerLevel) level, (ServerPlayer) player, (item) -> {
            });
            this.inputSlots.setItem(index, itemStack);
        }
    }

    private void shrinkStackInSlot(int index) {
        ItemStack itemstack = this.inputSlots.getItem(index);
        if (!itemstack.isEmpty()) {
            itemstack.shrink(1);
            this.inputSlots.setItem(index, itemstack);
        }
    }

    @Override
    protected void onTake(Player player, ItemStack itemStack) {
        itemStack.onCraftedBy(player.level(), player, itemStack.getCount());
        this.resultSlots.awardUsedRecipes(player, this.getRelevantItems());
        this.damageStackInSlot(CHISEL_SLOT);
        this.shrinkStackInSlot(TEMPLATE_SLOT);
        this.shrinkStackInSlot(BASE_SLOT);
        this.access.execute((level, blockPos) -> level.levelEvent(1044, blockPos, 0));
    }

    @Override
    protected boolean isValidBlock(BlockState blockState) {
        return blockState.is(ManaweaveAndRunesBlockInit.RUNE_CARVER_BLOCK);
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(CHISEL_SLOT, 33, 48, itemStack -> this.recipes.stream()
                        .anyMatch(value -> (value.value()).isChiselIngredient(itemStack)))
                .withSlot(TEMPLATE_SLOT, 51, 48, itemStack -> this.recipes.stream()
                        .anyMatch(value -> (value.value()).isTemplateIngredient(itemStack)))
                .withSlot(BASE_SLOT, 69, 48, itemStack -> this.recipes.stream()
                        .anyMatch(value -> (value.value()).isBaseIngredient(itemStack)))
                .withResultSlot(RESULT_SLOT, 123, 48)
                .build();
    }

    @Override
    public boolean canMoveIntoInputSlots(ItemStack stack) {
        return this.findSlotToQuickMoveTo(stack).isPresent();
    }

    public int getSlotToQuickMoveTo(ItemStack stack) {
        return this.findSlotToQuickMoveTo(stack).orElse(0);
    }

    @Override
    public void createResult() {
        RuneCarverRecipeInput runeCarverRecipeInput = this.createRecipeInput();
        List<RecipeHolder<RuneCarverRecipe>> list = this.level.getRecipeManager().getRecipesFor(ManaweaveAndRunesRecipeInit.RUNE_CARVER_RECIPE_TYPE.get(), runeCarverRecipeInput, this.level);
        if(list.isEmpty()){
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            RecipeHolder<RuneCarverRecipe> recipeHolder = list.getFirst();
            ItemStack itemstack = recipeHolder.value().assemble(runeCarverRecipeInput, this.level.registryAccess());
            if (itemstack.isItemEnabled(this.level.enabledFeatures())) {
                this.selectedRecipe = recipeHolder;
                this.resultSlots.setRecipeUsed(recipeHolder);
                this.resultSlots.setItem(0, itemstack);
            }
        }
    }

    private RuneCarverRecipeInput createRecipeInput() {
        return new RuneCarverRecipeInput(
                this.inputSlots.getItem(CHISEL_SLOT),
                this.inputSlots.getItem(BASE_SLOT),
                this.inputSlots.getItem(TEMPLATE_SLOT)
        );
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.inputSlots.getItem(CHISEL_SLOT), this.inputSlots.getItem(TEMPLATE_SLOT),
                       this.inputSlots.getItem(BASE_SLOT));
    }

    private OptionalInt findSlotToQuickMoveTo(ItemStack stack) {
        return this.recipes.stream()
                .flatMapToInt((recipeHolder) -> findSlotMatchingIngredient(recipeHolder.value(), stack).stream())
                .filter((index) -> !this.getSlot(index).hasItem())
                .findFirst();
    }
}
