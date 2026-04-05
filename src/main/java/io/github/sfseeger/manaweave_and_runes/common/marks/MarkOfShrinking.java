package io.github.sfseeger.manaweave_and_runes.common.marks;

import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MarkOfShrinking extends ScaleMark {
    private static final ResourceLocation MARK_OF_SHRINKING_MODIFIER_LOCATION =
            ManaweaveAndRunes.asResource("mark_of_shrinking_modifier");

    @Override
    public ResourceLocation getModifierLocation() {
        return MARK_OF_SHRINKING_MODIFIER_LOCATION;
    }

    @Override
    public double getHealthModifier(MarkInstance instance) {
        return Math.max(-6.0 * instance.getStrength(), -15.0);
    }

    @Override
    public double getScaleModifier(MarkInstance instance) {
        return Math.max(-(instance.getStrength() + 3) / 10.0, -0.6);
    }
}
