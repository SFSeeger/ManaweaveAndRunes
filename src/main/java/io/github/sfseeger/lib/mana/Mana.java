package io.github.sfseeger.lib.mana;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.mana.utils.ManaGenerationHelper;
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
    public static final Codec<Mana> CODEC;

    static {
        //  ManaRegistry.MANA_REGISTRY.holderByNameCodec().fieldOf("mana_id").forGetter(Mana::RegistryHolder)
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ManaProperties.CODEC.fieldOf("properties").forGetter(Mana::getProperties)
        ).apply(instance, Mana::new));
    }

    private final Holder.Reference<Mana> registryHolder;

    public boolean canBeGenerated() {
        return this.properties.canBeGenerated;
    }

    public int canGenerateMana(Level level, BlockPos pos, BlockState state) {
        // Returns 0 if mana cannot be generated, otherwise the factor of mana that can be generated
        // E.g when placing multiple lava blocks around a mana collector, the mana collector will generate more mana
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
        this.registryHolder = ManaRegistry.MANA_REGISTRY.createIntrusiveHolder(this);
    }

    private ManaProperties getProperties() {
        return properties;
    }

    public Holder.Reference<Mana> RegistryHolder() {
        return this.registryHolder;
    }
}
