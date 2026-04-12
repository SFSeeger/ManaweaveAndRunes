package io.github.sfseeger.lib.common.spells;

import com.mojang.serialization.Codec;
import io.github.sfseeger.lib.common.datamaps.SpellNodeAttributes;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

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

    private String descriptionId;

    public AbstractSpellNode() {
    }

    public MutableComponent getName() {
        return Component.translatable(this.getDescriptionId());
    }

    public MutableComponent getDescription() {
        return Component.literal("This is a spell node");
    }

    public Map<Mana, Integer> getManaCost() {
        return SpellNodeAttributes.getAttributesForSpellNode(this).map(SpellNodeAttributes::cost).orElse(Map.of());
    }

    public int getCooldown() {
        return SpellNodeAttributes.getAttributesForSpellNode(this).map(SpellNodeAttributes::baseCooldown).orElse(0);
    }

    public Set<AbstractSpellNode> getPossibleModifiers() {
        return SpellNodeAttributes.getAttributesForSpellNode(this)
                .map(SpellNodeAttributes::possibleModifiers)
                .orElse(Set.of());
    }

    public abstract @NotNull SpellNodeType getSpellNodeType();

    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId =
                    Util.makeDescriptionId("spell", ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY.getKey(this));
        }
        return this.descriptionId;
    }

    public ResourceLocation getRegistryName() {
        return ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY.getKey(this);
    }
}
