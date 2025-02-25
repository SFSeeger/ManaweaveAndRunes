package io.github.sfseeger.manaweave_and_runes.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    public static @NotNull BlockPos getRandomBlockPos(BlockPos pos, RandomSource random, Vec3 area) {
        return pos
                .offset(random.nextInt((int) area.x()) - (int) area.x() / 2,
                        random.nextInt((int) area.y()) - ((int) area.y() / 2 + random.nextInt((int) area.y())),
                        random.nextInt((int) area.z()) - (int) area.z() / 2);
    }
}
