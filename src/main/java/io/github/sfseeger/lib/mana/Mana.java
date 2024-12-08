package io.github.sfseeger.lib.mana;

import io.github.sfseeger.lib.mana.utils.ManaGenerationHelper;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Mana {

    String descriptionId;
    ManaProperties properties;

    public Mana(ManaProperties properties) {
        this.properties = properties;
    }

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

}
