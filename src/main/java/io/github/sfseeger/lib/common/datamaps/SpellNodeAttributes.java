package io.github.sfseeger.lib.common.datamaps;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger;
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Supplier;


// TODO: Figure out how we can increase type safety here, since modifers are always AbstractSpellModifiers
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record SpellNodeAttributes(Map<Mana, Integer> cost, int baseCooldown, Set<AbstractSpellNode> possibleModifiers) {
    public static final Codec<SpellNodeAttributes> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(Mana.MANA_MAP_CODEC.fieldOf("cost").forGetter(SpellNodeAttributes::cost),
                                       Codec.INT.fieldOf("baseCooldown").forGetter(SpellNodeAttributes::baseCooldown),
                                       Codec.list(AbstractSpellNode.CODEC)
                                               .xmap(Set::copyOf, ArrayList::new)
                                               .fieldOf("possibleModifiers")
                                               .forGetter(SpellNodeAttributes::possibleModifiers))
                    .apply(instance, SpellNodeAttributes::new));

    public static final AdvancedDataMapType<AbstractSpellNode, SpellNodeAttributes, SpellNodeAttributeRemover>
            SPELL_NODE_ATTRIBUTES = AdvancedDataMapType.builder(ManaweaveAndRunes.asResource("spell_node_attributes"),
                                                                ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY_KEY,
                                                                SpellNodeAttributes.CODEC)
            .merger(new SpellNodeAttributeMerger())
            .remover(SpellNodeAttributeRemover.CODEC)
            .synced(SpellNodeAttributes.CODEC, true)
            .build();

    public static Builder builder(int cooldown) {
        return new Builder(cooldown);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SpellNodeAttributes(
                Map<Mana, Integer> cost1, int cooldown, Set<AbstractSpellNode> modifiers
        ))) {
            return false;
        }
        return this.cost() == cost1 && this.baseCooldown() == cooldown && this.possibleModifiers().equals(modifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, baseCooldown, possibleModifiers);
    }

    public static class SpellNodeAttributeMerger implements DataMapValueMerger<AbstractSpellNode, SpellNodeAttributes> {

        @Override
        public SpellNodeAttributes merge(Registry<AbstractSpellNode> registry, Either<TagKey<AbstractSpellNode>, ResourceKey<AbstractSpellNode>> first, SpellNodeAttributes firstValue, Either<TagKey<AbstractSpellNode>, ResourceKey<AbstractSpellNode>> second, SpellNodeAttributes secondValue) {
            Map<Mana, Integer> mergedMap = new HashMap<>(firstValue.cost());
            secondValue.cost().forEach((mana, amount) -> mergedMap.merge(mana, amount, Integer::sum));

            int mergedCooldown = firstValue.baseCooldown() + secondValue.baseCooldown();

            Set<AbstractSpellNode> mergedModifiers =
                    new java.util.HashSet<>(Set.copyOf(firstValue.possibleModifiers()));
            mergedModifiers.addAll(secondValue.possibleModifiers());

            return new SpellNodeAttributes(mergedMap, mergedCooldown, mergedModifiers);
        }
    }

    public record SpellNodeAttributeRemover(Set<AbstractSpellNode> modifiersToRemove,
                                            Set<Mana> manaToRemove) implements DataMapValueRemover<AbstractSpellNode, SpellNodeAttributes> {
        public static final Codec<SpellNodeAttributes.SpellNodeAttributeRemover> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(Codec.list(AbstractSpellNode.CODEC)
                                                   .xmap(Set::copyOf, ArrayList::new)
                                                   .optionalFieldOf("modifiersToRemove", Set.of())
                                                   .forGetter(SpellNodeAttributeRemover::modifiersToRemove),
                                           Codec.list(Mana.CODEC)
                                                   .xmap(Set::copyOf, ArrayList::new)
                                                   .optionalFieldOf("manaToRemove", Set.of())
                                                   .forGetter(SpellNodeAttributeRemover::manaToRemove))
                        .apply(instance, SpellNodeAttributeRemover::new));

        @Override
        public Optional<SpellNodeAttributes> remove(SpellNodeAttributes spellNodeAttributes, Registry<AbstractSpellNode> registry, Either<TagKey<AbstractSpellNode>, ResourceKey<AbstractSpellNode>> either, AbstractSpellNode spellNode) {
            Map<Mana, Integer> newCost = new HashMap<>(spellNodeAttributes.cost());
            for (Mana key : this.manaToRemove()) {
                newCost.remove(key);
            }

            Set<AbstractSpellNode> newModifiers = new HashSet<>(spellNodeAttributes.possibleModifiers());
            newModifiers.removeAll(this.modifiersToRemove());

            return Optional.of(new SpellNodeAttributes(newCost, spellNodeAttributes.baseCooldown(), newModifiers));
        }
    }

    public static Optional<SpellNodeAttributes> getAttributesForSpellNode(AbstractSpellNode node) {
        Optional<Holder.Reference<AbstractSpellNode>>
                nodeHolder = ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY.getHolder(node.getRegistryName());
        if (nodeHolder.isEmpty()) return Optional.empty();
        SpellNodeAttributes attributes = nodeHolder.get().getData(SPELL_NODE_ATTRIBUTES);
        return Optional.ofNullable(attributes);
    }

    public static final class Builder {
        int cooldown;
        Set<AbstractSpellNode> possibleModifiers = new HashSet<>();
        Map<Mana, Integer> cost = new HashMap<>();

        public Builder(int cooldown) {
            this.cooldown = cooldown;
        }

        public Builder withCost(Mana mana, int amount) {
            this.cost.merge(mana, amount, Integer::sum);
            return this;
        }

        public <T extends AbstractSpellNode> Builder withModifier(T modifier) {
            if (!(modifier instanceof AbstractSpellModifier))
                throw new IllegalArgumentException("Modifier must be an AbstractSpellModifier");
            this.possibleModifiers.add(modifier);
            return this;
        }

        public <T extends AbstractSpellNode> Builder withModifier(Holder<T> modifier) {
            if (!(modifier.value() instanceof AbstractSpellModifier modifier1))
                throw new IllegalArgumentException("Modifier must be an AbstractSpellModifier");
            return withModifier(modifier1);
        }

        public <T extends AbstractSpellNode> Builder withModifier(Supplier<T> modifier) {
            if (!(modifier.get() instanceof AbstractSpellModifier modifier1))
                throw new IllegalArgumentException("Modifier must be an AbstractSpellModifier");
            this.possibleModifiers.add(modifier1);
            return this;
        }

        public SpellNodeAttributes build() {
            return new SpellNodeAttributes(cost, cooldown, possibleModifiers);
        }
    }
}