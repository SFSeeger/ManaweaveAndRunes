package io.github.sfseeger.manaweave_and_runes.client.particles.mana_particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

public class ManaParticleType extends ParticleType<ManaParticleOptions> {

    public ManaParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<ManaParticleOptions> codec() {
        return ManaParticleOptions.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ManaParticleOptions> streamCodec() {
        return ManaParticleOptions.STREAM_CODEC;
    }

    public static class Factory implements ParticleProvider<ManaParticleOptions> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public @Nullable Particle createParticle(ManaParticleOptions options, ClientLevel level,
                double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ManaParticle(level, x, y, z, options.red(), options.green(), options.blue(), options.gravity(),
                                    options.size(), options.maxAgeMul(), sprite);
        }
    }
}
