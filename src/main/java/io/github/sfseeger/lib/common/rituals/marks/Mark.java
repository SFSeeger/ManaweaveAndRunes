package io.github.sfseeger.lib.common.rituals.marks;


import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class Mark {
    private String descriptionId;

    public abstract void applyEffect(MarkInstance markInstance, Player player);

    public void applyClientEffect(MarkInstance markInstance, Player player) {
    }

    public void onMarkAdd(MarkInstance markInstance, Player player) {
    }

    public void onMarkRemove(MarkInstance markInstance, Player player) {
    }

    public abstract MarkType getMarkType();


    public MutableComponent getName() {
        return Component.translatable(getDescriptionId());
    }

    private String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("mark", ManaweaveAndRunesRegistries.MARK_REGISTRY.getKey(this));
        }
        return this.descriptionId;
    }
}
