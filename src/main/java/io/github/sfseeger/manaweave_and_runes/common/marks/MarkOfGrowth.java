package io.github.sfseeger.manaweave_and_runes.common.marks;

import io.github.sfseeger.lib.common.rituals.marks.Mark;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MarkOfGrowth extends ScaleMark {
    private static final ResourceLocation MARK_OF_GROWTH_MODIFIER_LOCATION =
            ManaweaveAndRunes.asResource("mark_of_growth_modifier");

    @Override
    public ResourceLocation getModifierLocation() {
        return MARK_OF_GROWTH_MODIFIER_LOCATION;
    }

    @Override
    public double getHealthModifier(MarkInstance instance) {
        return Math.min(6.0 * instance.getStrength(), 15.0);
    }

    @Override
    public double getScaleModifier(MarkInstance instance) {
        return Math.min( 1.0 + (instance.getStrength() + 3) / 10.0, 6.0);
    }
}
