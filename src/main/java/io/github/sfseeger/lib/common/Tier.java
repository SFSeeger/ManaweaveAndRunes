package io.github.sfseeger.lib.common;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

public enum Tier implements StringRepresentable {
    NONE(0),
    NOVICE(1),
    MASTER(2),
    ASCENDED(3);

    public static final Codec<Tier> CODEC = StringRepresentable.fromEnum(Tier::values);
    public static final StreamCodec<FriendlyByteBuf, Tier> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Tier.class);
    private Integer value;


    Tier(int value) {
        this.value = value;
    }

    public boolean greaterThanEqual(Tier other) {
        return this.value >= other.value;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name();
    }
}

