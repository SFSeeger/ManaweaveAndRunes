package io.github.sfseeger.manaweave_and_runes.common.api.mana;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ManaProperties {
    private long color;
    private @Nullable ResourceLocation icon;

    public ManaProperties(long color, @Nullable ResourceLocation icon) {
        this.color = color;
        this.icon = icon;
    }

    public static class Builder {
        private long color = 0x000000;
        private ResourceLocation icon;

        public Builder color(long color) {
            this.color = color;
            return this;
        }

        public Builder icon(ResourceLocation icon) {
            this.icon = icon;
            return this;
        }

        public ManaProperties build() {
            return new ManaProperties(color, icon);
        }
    }
}
