package io.github.sfseeger.lib.common.rituals.marks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class MarkInstance {
    public static final Codec<MarkInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ManaweaveAndRunesRegistries.MARK_REGISTRY.byNameCodec()
                            .fieldOf("markType")
                            .forGetter(m -> m.mark),
                    Codec.INT.fieldOf("strength")
                            .forGetter(m -> m.strength))
            .apply(instance, MarkInstance::new));
    private final Mark mark;
    private int strength = 1;

    public MarkInstance(Mark mark, int strength) {
        this.mark = mark;
        this.strength = strength;
    }

    public MarkInstance(Mark mark) {
        this(mark, 1);
    }

    public void applyEffect(Player player) {
        mark.applyEffect(this, player);
    }

    public void applyClientEffect(Player player) {
        mark.applyClientEffect(this, player);
    }

    public void onMarkAdd(Player player) {
        mark.onMarkAdd(this, player);
    }
    public void onMarkRemove(Player player) {
        mark.onMarkRemove(this, player);
    }

    public int getStrength() {
        return strength;
    }
    public void setStrength(int strength) {
        this.strength = strength;
    }
    public MutableComponent getName() {
        return mark.getName();
    }

    public MarkType getMarkType() {
        return mark.getMarkType();
    }
}
