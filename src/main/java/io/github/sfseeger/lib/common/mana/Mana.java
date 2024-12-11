package io.github.sfseeger.lib.common.mana;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.sfseeger.lib.common.mana.utils.ManaGenerationHelper;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Mana {

    String descriptionId;
    ManaProperties properties;
    public static final Codec<Holder<Mana>> CODEC;

    static {
        //  ManaRegistry.MANA_REGISTRY.holderByNameCodec().fieldOf("mana_id").forGetter(Mana::RegistryHolder)
        CODEC = Codec.lazyInitialized(() ->
                ManaRegistry.MANA_REGISTRY.holderByNameCodec().validate(
                        instance -> instance.is(Manas.EmptyMana.registryHolder()) ?
                                DataResult.error(() -> "Empty mana cannot be serialized") : DataResult.success(instance)
                )
        );
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

            for (ManaGenerationHelper.GenerationCondition condition : properties.generationConditions) {
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

    public Mana(ManaProperties properties) {
        this.properties = properties;
    }

    private ManaProperties getProperties() {
        return properties;
    }

    public Holder<Mana> registryHolder() {
        return ManaRegistry.MANA_REGISTRY.wrapAsHolder(this);
    }
}
