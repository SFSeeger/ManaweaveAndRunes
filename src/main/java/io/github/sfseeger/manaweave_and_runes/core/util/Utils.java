package io.github.sfseeger.manaweave_and_runes.core.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    public static boolean compareIngredientsToItems(List<Ingredient> input, List<ItemStack> supplied){
        List<ItemStack> unmatchedItemStacks = new ArrayList<>(supplied);
        // Check each ingredient in the ingredient list
        for (Ingredient ingredient : input) {
            boolean matched = false;
            // Try to find a matching ItemStack for this ingredient
            for (ItemStack stack : unmatchedItemStacks) {
                if (ingredient.test(stack)) {
                    matched = true;
                    unmatchedItemStacks.remove(stack); // Remove the matched stack
                    break;
                }
            }
            // If no match was found for this ingredient, return false
            if (!matched) {
                return false;
            }
        }

        // If there are any unmatched ItemStacks remaining, return false
        return unmatchedItemStacks.isEmpty();
    }
}
