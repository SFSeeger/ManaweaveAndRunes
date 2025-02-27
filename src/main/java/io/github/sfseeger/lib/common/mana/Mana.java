package io.github.sfseeger.lib.common.mana;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.sfseeger.lib.common.mana.generation.AbstractManaGenerationCondition;
import io.github.sfseeger.lib.common.mana.generation.ManaGenerationHelper;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mana {

    String descriptionId;
    ManaProperties properties;
    public static final Codec<Holder<Mana>> CODEC;
    public static final Codec<List<Pair<Holder<Mana>, Integer>>> MANAS_WITH_AMOUNT_CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<Mana, Integer>> MANA_MAP_STREAM_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.registry(ManaweaveAndRunesRegistries.MANA_REGISTRY_KEY),
                    ByteBufCodecs.INT,
                    256
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<Holder<Mana>, Integer>> MANA_HOLDER_MAP_STREAM_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.holderRegistry(ManaweaveAndRunesRegistries.MANA_REGISTRY_KEY),
                    ByteBufCodecs.INT,
                    256
            );

    static {
        //  ManaRegistry.MANA_REGISTRY.holderByNameCodec().fieldOf("mana_id").forGetter(Mana::RegistryHolder)
        CODEC = Codec.lazyInitialized(() ->
                                              ManaweaveAndRunesRegistries.MANA_REGISTRY.holderByNameCodec().validate(
                                                      instance -> instance.is(Manas.EmptyMana.registryHolder()) ?
                                                              DataResult.error(
                                                                      () -> "Empty mana cannot be serialized") : DataResult.success(
                                                              instance)
                                              )
        );
        MANAS_WITH_AMOUNT_CODEC = Codec.list(
                Codec.pair(
                        Mana.CODEC.fieldOf("manaType").codec(),
                        Codec.INT.fieldOf("amount").codec()
                )
        );
    }

    public static Map<Mana, Integer> manaMapFromList(List<Pair<Holder<Mana>, Integer>> list) {
        return list.stream().collect(Collectors.toMap(pair -> pair.getFirst().value(), Pair::getSecond));
    }

    public static List<Pair<Holder<Mana>, Integer>> manaMapAsList(Map<Mana, Integer> map) {
        return map.entrySet()
                .stream()
                .map(entry -> Pair.of(entry.getKey().registryHolder(), entry.getValue()))
                .toList();
    }

    public boolean canBeGenerated() {
        return this.properties.canBeGenerated;
    }

    public int canGenerateMana(Level level, BlockPos pos, BlockState state) {
        // Returns 0 if mana cannot be generated, otherwise the factor of mana that can be generated
        // E.g. when placing multiple lava blocks around a mana collector, the mana collector will generate more mana
        if (!this.canBeGenerated()) {
            return 0;
        }

        int potentialGeneration = 0;
        int fulfilledConditions = 0;

        if (!properties.generationConditions.isEmpty()) {

            for (AbstractManaGenerationCondition condition : properties.generationConditions) {
                if (properties.modi == ManaGenerationHelper.GenerationConditionModi.AND) {
                    int _potentialGeneration = condition.getManaGenerationPotential(level, pos, state);
                    if (_potentialGeneration == 0) {
                        return 0;
                    }
                    potentialGeneration = Math.max(potentialGeneration, _potentialGeneration);
                } else {
                    potentialGeneration += condition.getManaGenerationPotential(level, pos, state);
                    if (potentialGeneration > 0) {
                        fulfilledConditions++;
                    }
                }
            }
            if (properties.modi == ManaGenerationHelper.GenerationConditionModi.XOR) {
                if (fulfilledConditions == properties.generationConditions.size()) {
                    return 0;
                }
            }
        }

        return potentialGeneration;
    }

    public ManaProperties properties() {
        return this.properties;
    }

    public int getGenerationMultiplier() {
        // Returns a factor by which the generation amount is multiplied
        return this.properties.generationMultiplier;
    }

    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("mana", ManaweaveAndRunesRegistries.MANA_REGISTRY.getKey(this));
        }
        return this.descriptionId;
    }

    public MutableComponent getName() {
        return Component.translatable(getDescriptionId());
    }

    public String toString() {
        return "Mana{" + ManaweaveAndRunesRegistries.MANA_REGISTRY.getKey(this) + "}";
    }

    public Mana(ManaProperties properties) {
        this.properties = properties;
    }

    private ManaProperties getProperties() {
        return properties;
    }

    public Holder<Mana> registryHolder() {
        return ManaweaveAndRunesRegistries.MANA_REGISTRY.wrapAsHolder(this);
    }
}
