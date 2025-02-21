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
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

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
        super(ManaweaverAndRunesMenuInit.RUNE_CARVER_BLOCK_MENU, containerId, playerInventory, access);
        this.level = playerInventory.player.level();
        this.recipes = this.level.getRecipeManager().getAllRecipesFor(ManaweaveAndRunesRecipeInit.RUNE_CARVER_RECIPE_TYPE.get());
    }

    @Override
    protected boolean mayPickup(Player player, boolean b) {
        return false;
    }

    @Override
    protected void onTake(Player player, ItemStack itemStack) {
        itemStack.onCraftedBy(player.level(), player, itemStack.getCount());
        this.resultSlots.awardUsedRecipes(player, this.getRelevantItems());
        this.shrinkStackInSlot(CHISEL_SLOT);
        this.shrinkStackInSlot(TEMPLATE_SLOT);
        this.shrinkStackInSlot(BASE_SLOT);
        this.access.execute((level, blockPos) -> {
            level.levelEvent(1044, blockPos, 0);
        });
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

    private List<ItemStack> getRelevantItems() {
        return List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1), this.inputSlots.getItem(2));
    }

    @Override
    protected boolean isValidBlock(BlockState blockState) {
        return blockState.is(ManaweaveAndRunesBlockInit.RUNE_CARVER_BLOCK);
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

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(CHISEL_SLOT, 8, 48, itemStack -> this.recipes.stream().anyMatch(value -> (value.value()).isChiselIngredient(itemStack)))
                .withSlot(TEMPLATE_SLOT, 26, 48, itemStack -> this.recipes.stream().anyMatch(value -> (value.value()).isTemplateIngredient(itemStack)))
                .withSlot(BASE_SLOT, 44, 48, itemStack -> this.recipes.stream().anyMatch(value -> (value.value()).isBase(itemStack)))
                .withResultSlot(RESULT_SLOT, 98, 48)
                .build();
    }
}
