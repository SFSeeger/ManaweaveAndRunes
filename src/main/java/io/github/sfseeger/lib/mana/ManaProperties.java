package io.github.sfseeger.lib.mana;

import io.github.sfseeger.lib.mana.utils.ManaGenerationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ManaProperties {
    protected long color;
    protected @Nullable ResourceLocation icon;
    protected boolean canBeGenerated = false;
    protected int generationMultiplier = 1;
    protected List<ManaGenerationHelper.GenerationCondition> generationConditions;
    ManaGenerationHelper.GenerationConditionModi modi = ManaGenerationHelper.GenerationConditionModi.OR;

    protected ManaProperties(long color, @Nullable ResourceLocation icon) {
        this.color = color;
        this.icon = icon;
    }

    public static class Builder {
        List<ManaGenerationHelper.GenerationCondition> generationConditions = new ArrayList<>();
        ManaGenerationHelper.GenerationConditionModi modi = ManaGenerationHelper.GenerationConditionModi.OR;
        private long color = 0x000000;
        private ResourceLocation icon;
        private boolean canBeGenerated = false;
        private int generationMultiplier = 1;

        public Builder color(long color) {
            this.color = color;
            return this;
        }

        public Builder icon(ResourceLocation icon) {
            this.icon = icon;
            return this;
        }

        public Builder canBeGenerated(boolean canBeGenerated) {
            this.canBeGenerated = canBeGenerated;
            return this;
        }

        public Builder generationMultiplier(int generationMultiplier) {
            this.generationMultiplier = generationMultiplier;
            return this;
        }

        public Builder addGenerationCondition(ManaGenerationHelper.GenerationCondition condition) {
            generationConditions.add(condition);
            return this;
        }

        public Builder generationConditionModi(ManaGenerationHelper.GenerationConditionModi modi) {
            this.modi = modi;
            return this;
        }

        public ManaProperties build() {
            ManaProperties p = new ManaProperties(color, icon);
            p.canBeGenerated = canBeGenerated;
            p.generationMultiplier = generationMultiplier;
            p.generationConditions = generationConditions;
            p.modi = modi;
            return p;
        }
    }
}
