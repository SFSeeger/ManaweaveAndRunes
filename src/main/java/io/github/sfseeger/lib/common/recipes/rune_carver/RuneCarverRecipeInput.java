package io.github.sfseeger.lib.common.recipes.rune_carver;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record RuneCarverRecipeInput(ItemStack chisel, ItemStack runeBasePlate, ItemStack runeTemplate) implements RecipeInput {

    @Override
    public ItemStack getItem(int slot) {
        return switch (slot) {
            case 0 -> this.chisel();
            case 1 -> this.runeBasePlate();
            case 2 -> this.runeTemplate();
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 3;
    }
}
