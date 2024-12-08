package io.github.sfseeger.manaweave_and_runes.common.api.mana;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Mana {

    String descriptionId;
    ManaProperties properties;

    public Mana(ManaProperties properties) {
        this.properties = properties;
    }

    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("mana", ManaRegistry.MANA_REGISTRY.getKey(this));
        }
        return this.descriptionId;
    }

    public MutableComponent getName() {
        return Component.translatable(getDescriptionId());
    }

    public String toString() {
        return "Mana{" + ManaRegistry.MANA_REGISTRY.getKey(this) + "}";
    }

}
