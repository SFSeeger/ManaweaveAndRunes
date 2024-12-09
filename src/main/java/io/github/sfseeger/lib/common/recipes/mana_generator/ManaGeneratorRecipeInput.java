package io.github.sfseeger.lib.common.recipes.mana_generator;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record ManaGeneratorRecipeInput(ItemStack stack) implements RecipeInput {
    @Override
    public ItemStack getItem(int slot) {
        if(slot != 0 ) throw new IllegalArgumentException("No item for slot: " + slot);
        return this.stack();
    }

    @Override
    public int size() {
        return 1;
    }
}
