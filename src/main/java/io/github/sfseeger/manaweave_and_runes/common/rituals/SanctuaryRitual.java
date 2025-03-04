package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.manaweave_and_runes.client.particles.mana_particle.ManaParticleOptions;
import io.github.sfseeger.manaweave_and_runes.core.util.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static io.github.sfseeger.manaweave_and_runes.common.MRDamageTypes.createRitualFailure;

public class SanctuaryRitual extends Ritual {
    public SanctuaryRitual() {
        super(Tier.MASTER, -1);
    }

    public SanctuaryRitual(Tier tier) {
        super(tier, -1);
    }

    private static boolean isMonster(Entity entity) {
        return entity instanceof Mob && entity.getType().getCategory().equals(MobCategory.MONSTER);
    }

    @Override
    public Vec3 getDimension() {
        return new Vec3(30, 30, 30);
    }

    @Override
    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {
        if (ticksPassed % 15 != 0) return RitualStepResult.SKIP;
        AABB boundingBox = new AABB(pos).inflate(getDimension().length() / 2);
        Vec3 ritualCenter = new Vec3(pos.getX(), pos.getY(), pos.getZ());

        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, boundingBox, SanctuaryRitual::isMonster);
        mobs.forEach(mob -> {
            Vec3 mobPos = mob.position();
            Vec3 direction = mobPos.subtract(ritualCenter).normalize().scale(2f);
            mob.setDeltaMovement(direction);
            mob.hurtMarked = true;
        });

        level.getNearbyPlayers(TargetingConditions.forNonCombat().ignoreLineOfSight(), null, boundingBox)
                .forEach(this::addEffectToPlayer);

        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualClientTick(Level level, BlockPos pos, BlockState state, int ticksPassed, RitualContext context,
            RitualOriginType originType) {
        Vec3 d = getDimension();
        RandomSource random = level.getRandom();

        for (int i = 0; i < 15; i++) {
            Vec3 t = ParticleUtils.randomPosInsideSphere(pos, random, d.length() / 2);
            level.addParticle(new ManaParticleOptions(Math.min(random.nextFloat(), 0.5f), 0f, 1f, 0.1f, -0.1f,
                                                      random.nextFloat() + 0.8f), t.x(), t.y(), t.z(), 0, 0, 0);
        }
    }

    @Override
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {

    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        RitualUtils.getStartingPlayer(level, context).ifPresent(player -> {
            player.removeEffect(MobEffects.ABSORPTION);
            player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 1, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, false, true));
            player.hurt(createRitualFailure(level, pos), 4);
        });
    }

    protected void addEffectToPlayer(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 3, true, false));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1, true, false));
    }
}
