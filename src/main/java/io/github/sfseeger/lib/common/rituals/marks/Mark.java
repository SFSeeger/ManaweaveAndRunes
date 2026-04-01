package io.github.sfseeger.lib.common.rituals.marks;


import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class Mark {
    public abstract void applyEffect(MarkInstance markInstance, Player player);
    public void applyClientEffect(MarkInstance markInstance, Player player) {

    }
    public abstract MarkType getMarkType();

}
