package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;

public enum SpellCastingResult {
    SKIPPED(true),
    SUCCESS(true),
    FAILURE(false);

    private final boolean success;

    SpellCastingResult(boolean success) {
        this.success = success;
    }
    public SpellCastingResult compare(SpellCastingResult other){
        return this.ordinal() >= other.ordinal() ? this: other;
    }

    public boolean isSuccess() {
        return success;
    }

    public InteractionResultHolder<ItemStack> returnForResult(ItemStack stack) {
        switch (this) {
            case SUCCESS:
                return InteractionResultHolder.success(stack);
            case FAILURE:
                return InteractionResultHolder.fail(stack);
            default:
                return InteractionResultHolder.pass(stack);
        }
    }
}
