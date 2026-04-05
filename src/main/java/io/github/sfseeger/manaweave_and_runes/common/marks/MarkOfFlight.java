package io.github.sfseeger.manaweave_and_runes.common.marks;

import io.github.sfseeger.lib.common.rituals.marks.Mark;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.lib.common.rituals.marks.MarkType;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForgeMod;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MarkOfFlight extends Mark {
    public static final ResourceLocation MARK_OF_FLYING_ATTRIBUTE_MODIFIER_ID =
            ManaweaveAndRunes.asResource("mark_of_flying_attribute_modifier");

    @Override
    public void applyEffect(MarkInstance markInstance, Player player) {
    }

    @Override
    public void onMarkAdd(MarkInstance markInstance, Player player) {
        AttributeInstance attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (attribute != null) {
            attribute.addOrReplacePermanentModifier(new AttributeModifier(
                    MARK_OF_FLYING_ATTRIBUTE_MODIFIER_ID,
                    1.0,
                    AttributeModifier.Operation.ADD_VALUE
            ));
        }
    }

    @Override
    public void onMarkRemove(MarkInstance markInstance, Player player) {
        AttributeInstance attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (attribute != null) {
            attribute.removeModifier(MARK_OF_FLYING_ATTRIBUTE_MODIFIER_ID);
        }
    }

    @Override
    public MarkType getMarkType() {
        return MarkType.BOON;
    }
}
