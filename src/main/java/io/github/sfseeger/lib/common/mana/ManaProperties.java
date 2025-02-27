package io.github.sfseeger.lib.common.mana;

import io.github.sfseeger.lib.common.mana.generation.AbstractManaGenerationCondition;
import io.github.sfseeger.lib.common.mana.generation.ManaGenerationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManaProperties {
    //    public static final Codec<ManaProperties> CODEC;
//
//    static {
//        CODEC = RecordCodecBuilder.create(instance -> instance.group(
//                Codec.INT.fieldOf("color").forGetter(ManaProperties::getColor),
//                ResourceLocation.CODEC.optionalFieldOf("icon").forGetter(ManaProperties::getIcon),
//                Codec.BOOL.optionalFieldOf("canBeGenerated", false).forGetter(ManaProperties::canBeGenerated),
//                Codec.INT.optionalFieldOf("generationMultiplier", 1).forGetter(ManaProperties::getGenerationMultiplier),
//                Codec.list(ManaGenerationHelper.GenerationCondition.CODEC)
//                        .optionalFieldOf("generationConditions", new ArrayList<>())
//                        .forGetter(ManaProperties::getGenerationConditions),
//                ManaGenerationHelper.GenerationConditionModi.CODEC.optionalFieldOf("generationConditionModi",
//                                                                                   ManaGenerationHelper.GenerationConditionModi.OR)
//                        .forGetter(ManaProperties::getGenerationConditionModi)
//        ).apply(instance, ManaProperties::new));
//    }
    protected ResourceLocation icon;
    protected boolean canBeGenerated;
    protected int generationMultiplier;
    protected List<AbstractManaGenerationCondition> generationConditions;
    ManaGenerationHelper.GenerationConditionModi modi = ManaGenerationHelper.GenerationConditionModi.OR;

    protected int color;

    protected ManaProperties(int color, @Nullable ResourceLocation icon, boolean canBeGenerated,
            int generationMultiplier,
            List<AbstractManaGenerationCondition> generationConditions,
            ManaGenerationHelper.GenerationConditionModi modi) {
        this.color = color;
        this.icon = icon == null ? ResourceLocation.fromNamespaceAndPath("manaweave_and_runes",
                                                                         "container/mana/default_mana") : icon;
        this.canBeGenerated = canBeGenerated;
        this.generationMultiplier = generationMultiplier;
        this.generationConditions = generationConditions;
        this.modi = modi;
    }

    public static ManaProperties Empty(){
        return new ManaProperties.Builder().build();
    }

    public ManaGenerationHelper.GenerationConditionModi getGenerationConditionModi() {
        return modi;
    }

    public List<AbstractManaGenerationCondition> getGenerationConditions() {
        return generationConditions;
    }

    public Integer getGenerationMultiplier() {
        return generationMultiplier;
    }

    public Boolean canBeGenerated() {
        return canBeGenerated;
    }

    public Optional<ResourceLocation> getIcon() {
        return Optional.ofNullable(icon);
    }

    public Integer getColor() {
        return color;
    }

    public static class Builder {
        List<AbstractManaGenerationCondition> generationConditions = new ArrayList<>();
        ManaGenerationHelper.GenerationConditionModi modi = ManaGenerationHelper.GenerationConditionModi.OR;
        private int color = 0x000000;
        private ResourceLocation icon = null;
        private boolean canBeGenerated = false;
        private int generationMultiplier = 1;

        public Builder color(int color) {
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

        public Builder addGenerationCondition(AbstractManaGenerationCondition condition) {
            generationConditions.add(condition);
            return this;
        }

        public Builder generationConditionModi(ManaGenerationHelper.GenerationConditionModi modi) {
            this.modi = modi;
            return this;
        }

        public ManaProperties build() {
            ManaProperties p =
                    new ManaProperties(color, icon, canBeGenerated, generationMultiplier, generationConditions, modi);
            return p;
        }
    }
}
