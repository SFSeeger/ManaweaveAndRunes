package io.github.sfseeger.lib.common.recipes.mana_concentrator;

import io.github.sfseeger.lib.common.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record ManaConcentratorInput(List<ItemStack> items, Tier tier) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public int size() {
        return this.items.size();
    }

    public boolean matches(Tier tier, List<Ingredient> inputs) {
        return tier.greaterThanEqual(this.tier) && inputs.size() == this.items.size() && inputs.stream()
                .allMatch(ingredient -> this.items.stream().anyMatch(ingredient));
    }
}
