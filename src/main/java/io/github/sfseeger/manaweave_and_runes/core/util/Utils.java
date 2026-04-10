package io.github.sfseeger.manaweave_and_runes.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
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

    public static VoxelShape rotateShape(Direction to, VoxelShape shape) {
        final VoxelShape[] shapeBuffer = {Shapes.empty()};
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            VoxelShape newShape = Shapes.empty();
            switch (to) {
                case UP -> newShape = Shapes.create(minX, minY, minZ, maxX, maxY, maxZ);
                case DOWN -> newShape = Shapes.create(minX, 1 - maxY, 1 - maxZ, maxX, 1 - minY, 1 - minZ);
                case NORTH -> newShape = Shapes.create(1 - maxX, minZ, 1 - maxY, 1 - minX, maxZ, 1 - minY);
                case SOUTH -> newShape = Shapes.create(minX, minZ, minY, maxX, maxZ, maxY);
                case EAST -> newShape = Shapes.create(1 - maxY, minZ, minX, 1 - minY, maxZ, maxX);
                case WEST -> newShape = Shapes.create(minY, minZ, minX, maxY, maxZ, maxX);
            }
            shapeBuffer[0] = Shapes.join(shapeBuffer[0], newShape, BooleanOp.OR);
        });
        return shapeBuffer[0];
    }
}
