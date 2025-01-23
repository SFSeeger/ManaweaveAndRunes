package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.Set;

public abstract class AbstractSpellNode {
    private final Map<Mana, Integer> baseCosts;
    private final int baseCooldown;
    private String descriptionId;

    public AbstractSpellNode(Map<Mana, Integer> baseCosts, int baseCooldown) {
        this.baseCosts = baseCosts;
        this.baseCooldown = baseCooldown;
    }


    public Component getDescription() {
        return Component.literal("This is a spell node");
    }

    public Map<Mana, Integer> getManaCosts() {
        return baseCosts;
    }

    public int getCooldown() {
        return baseCooldown;
    }

    public abstract Set<AbstractSpellNode> getPossibleModifiers();
}
