package io.github.sfseeger.manaweave_and_runes.common.marks;

import io.github.sfseeger.lib.common.rituals.marks.Mark;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.lib.common.rituals.marks.MarkType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ScaleMark extends Mark {
    @Override
    public void applyEffect(MarkInstance markInstance, Player player) {
    }

    @Override
    public void onMarkAdd(MarkInstance markInstance, Player player) {
        AttributeInstance attribute = player.getAttribute(Attributes.SCALE);
        if (attribute != null) {
            attribute.addOrReplacePermanentModifier(new AttributeModifier(
                    getModifierLocation(),
                    getScaleModifier(markInstance),
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            );
        }
        attribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (attribute != null) {
            attribute.addOrUpdateTransientModifier(new AttributeModifier(
                    getModifierLocation(),
                    getHealthModifier(markInstance),
                    AttributeModifier.Operation.ADD_VALUE)
            );
        }
    }

    @Override
    public void onMarkRemove(MarkInstance markInstance, Player player) {
        AttributeInstance attribute = player.getAttribute(Attributes.SCALE);
        if (attribute != null) {
            attribute.removeModifier(getModifierLocation());
        }
        attribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (attribute != null) {
            attribute.removeModifier(getModifierLocation());
        }
    }

    @Override
    public MarkType getMarkType() {
        return MarkType.CURSE;
    }

    public abstract ResourceLocation getModifierLocation();
    public abstract double getHealthModifier(MarkInstance instance);
    public abstract double getScaleModifier(MarkInstance instance);
}
