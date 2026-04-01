package io.github.sfseeger.manaweave_and_runes.marks;

import io.github.sfseeger.lib.common.rituals.marks.Mark;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.lib.common.rituals.marks.MarkType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MarkOfSinking extends Mark {
    @Override
    public void applyEffect(MarkInstance markInstance, Player player) {
        if (player.isInWater() || !player.onGround()) {
            Vec3 velocity = player.getDeltaMovement();
            player.setSwimming(false);
            boolean performChange = velocity.y != 0;
            if (velocity.y < 0) {
               velocity = velocity.multiply(1, 1.0 + Math.min(0.1 * markInstance.getStrength(), 0.4), 1);
            } else if (velocity.y > 0) {
                velocity = velocity.multiply(1, 1.0 - Math.min(0.1 * markInstance.getStrength(), 0.4), 1);
            }
            if (performChange){
                player.setDeltaMovement(velocity);
                player.hurtMarked = true;
            }
        }
    }

    @Override
    public MarkType getMarkType() {
        return MarkType.CURSE;
    }
}
