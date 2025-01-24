package io.github.sfseeger.lib.common.spells;

import com.mojang.serialization.Codec;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.Set;

public abstract class AbstractSpellNode {
    public static final Codec<AbstractSpellNode> CODEC =
            Codec.lazyInitialized(ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY::byNameCodec);

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

    public Map<Mana, Integer> getManaCost() {
        return baseCosts;
    }

    public int getCooldown() {
        return baseCooldown;
    }

    public abstract Set<AbstractSpellNode> getPossibleModifiers();

    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("mana", ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY.getKey(this));
        }
        return this.descriptionId;
    }
}
