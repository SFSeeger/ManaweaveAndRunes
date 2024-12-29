package io.github.sfseeger.manaweave_and_runes.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SonicBoomParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaConcentratedParticle extends SonicBoomParticle {
    public ManaConcentratedParticle(ClientLevel level, double x, double y, double z,
            double quadSizeMultiplier, SpriteSet sprites) {
        super(level, x, y, z, quadSizeMultiplier, sprites);
    }

    @OnlyIn(Dist.CLIENT)
    public static class ManaConcentratedProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public ManaConcentratedProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ManaConcentratedParticle(level, x, y, z, xSpeed, this.spriteSet);
        }
    }
}
