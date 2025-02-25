package io.github.sfseeger.manaweave_and_runes.client.particles.mana_particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

// THIS IS A COPY FROM BOTANIA https://github.com/VazkiiMods/Botania/blob/1.20.x/Xplat/src/main/java/vazkii/botania/client/fx/WispParticleType.java

@OnlyIn(Dist.CLIENT)
public class ManaParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final float moteParticleScale;
    private final int moteHalfLife;

    protected ManaParticle(ClientLevel level, double x, double y, double z, float red, float green, float blue,
            float gravity, float size, float maxAgeMul, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.gravity = gravity;
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.alpha = 0.375F;
        this.quadSize = (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F * size;
        this.moteParticleScale = quadSize;
        this.lifetime = (int) (28D / (Math.random() * 0.3D + 0.7D) * maxAgeMul);
        this.moteHalfLife = lifetime / 2;
        setSize(0.01F, 0.01F);

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(spriteSet);

        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float agescale = (float) age / (float) moteHalfLife;
        if (agescale > 1F) {
            agescale = 2 - agescale;
        }

        quadSize = moteParticleScale * agescale * 0.5F;
        return quadSize;
    }
}
