package io.github.sfseeger.manaweave_and_runes.client.particles.mana_particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.sfseeger.manaweave_and_runes.core.init.MRParticleTypeInit;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ManaParticleOptions(float size, float red, float green, float blue, float gravity,
                                  float maxAgeMul) implements ParticleOptions {
    public static final MapCodec<ManaParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(ManaParticleOptions::size),
            Codec.FLOAT.fieldOf("r").forGetter(ManaParticleOptions::red),
            Codec.FLOAT.fieldOf("g").forGetter(ManaParticleOptions::green),
            Codec.FLOAT.fieldOf("b").forGetter(ManaParticleOptions::blue),
            Codec.FLOAT.optionalFieldOf("gravity", 0.0f).forGetter(ManaParticleOptions::gravity),
            Codec.FLOAT.fieldOf("maxAgeMul").forGetter(ManaParticleOptions::maxAgeMul)
    ).apply(instance, ManaParticleOptions::new));

    public static final StreamCodec<ByteBuf, ManaParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ManaParticleOptions::size,
            ByteBufCodecs.FLOAT, ManaParticleOptions::red,
            ByteBufCodecs.FLOAT, ManaParticleOptions::green,
            ByteBufCodecs.FLOAT, ManaParticleOptions::blue,
            ByteBufCodecs.FLOAT, ManaParticleOptions::gravity,
            ByteBufCodecs.FLOAT, ManaParticleOptions::maxAgeMul,
            ManaParticleOptions::new
    );

    @Override
    public ParticleType<?> getType() {
        return MRParticleTypeInit.MANA_PARTICLE.get();
    }
}
