package io.github.sfseeger.lib.common.spells;

import com.mojang.serialization.Codec;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.Set;

public abstract class AbstractSpellNode {
    public static final Codec<AbstractSpellNode> CODEC =
            Codec.lazyInitialized(ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY::byNameCodec);

    public static final Codec<Holder<AbstractSpellNode>> HOLDER_CODEC =
            Codec.lazyInitialized(ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY::holderByNameCodec);

    public static final StreamCodec<RegistryFriendlyByteBuf, AbstractSpellNode>
            STREAM_CODEC = ByteBufCodecs.registry(ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<AbstractSpellNode>>
            HOLDER_STREAM_CODEC = ByteBufCodecs.holderRegistry(ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY_KEY);

    private final Map<Mana, Integer> baseCosts;
    private final int baseCooldown;
    private String descriptionId;

    public AbstractSpellNode(Map<Mana, Integer> baseCosts, int baseCooldown) {
        this.baseCosts = baseCosts;
        this.baseCooldown = baseCooldown;
    }

    public MutableComponent getName() {
        return Component.translatable(this.getDescriptionId());
    }

    public MutableComponent getDescription() {
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
            this.descriptionId = Util.makeDescriptionId("spell", ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY.getKey(this));
        }
        return this.descriptionId;
    }
}
