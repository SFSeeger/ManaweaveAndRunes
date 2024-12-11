package io.github.sfseeger.lib.common.recipes.rune_carver;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record RuneCarverRecipeInput(ItemStack chisel, ItemStack runeBasePlate) implements RecipeInput {

    @Override
    public ItemStack getItem(int slot) {
        return switch (slot) {
            case 0 -> this.chisel();
            case 1 -> this.runeBasePlate();
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
