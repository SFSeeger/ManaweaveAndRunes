package io.github.sfseeger.lib.common.entities.projectiles;

import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import io.github.sfseeger.lib.common.spells.SpellResolver;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import static io.github.sfseeger.lib.common.entities.ManaWeaveAndRuneEntityTypes.SPELL_PROJECTILE;

public class SpellProjectileEntity extends Projectile {
    private SpellResolver resolver;
    private SpellCastingContext context;

    public SpellProjectileEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public SpellProjectileEntity(Level level, SpellCastingContext context, SpellResolver resolver) {
        this(SPELL_PROJECTILE, level);
        this.context = context;
        this.resolver = resolver;
        LivingEntity caster = context.getCaster();
        this.setOwner(caster);
        this.setPos(caster.getX(), caster.getY() + caster.getEyeHeight(), caster.getZ());
    }

    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.level()
                        .addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25, d1 - vec3.y * 0.25, d2 - vec3.z * 0.25,
                                     vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vec3.scale(f));
        this.applyGravity();
        this.setPos(d0, d1, d2);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            resolver.resolve(result, context);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            resolver.resolve(result, context);
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        double d0 = packet.getXa();
        double d1 = packet.getYa();
        double d2 = packet.getZa();

        for(int i = 0; i < 7; ++i) {
            double d3 = 0.4 + 0.1 * (double)i;
            this.level().addParticle(ParticleTypes.GLOW, this.getX(), this.getY(), this.getZ(), d0 * d3, d1, d2 * d3);
        }

        this.setDeltaMovement(d0, d1, d2);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }
}
