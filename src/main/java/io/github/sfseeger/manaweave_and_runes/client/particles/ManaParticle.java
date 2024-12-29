package io.github.sfseeger.manaweave_and_runes.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlyTowardsPositionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaParticle extends FlyTowardsPositionParticle {
    private final SpriteSet spriteSet;

    protected ManaParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed,
            double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.spriteSet = spriteSet;

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 204;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.spriteSet);

        super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    public static class ManaParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public ManaParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ManaParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}
