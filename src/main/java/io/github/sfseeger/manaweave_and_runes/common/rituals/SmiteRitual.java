package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.util.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class SmiteRitual extends Ritual {

    public SmiteRitual() {
        super(Tier.NOVICE, 5 * 20);
    }

    @Override
    public Vec3 getDimension() {
        return new Vec3(255, 255, 255);
    }

    @Override
    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {
        UUID playerUUID = context.getData(RitualDataTypes.PLAYER_TYPE).getPlayerUUID();
        Player player = level.getPlayerByUUID(playerUUID);
        if (player != null && player.isAlive() && pos.distManhattan(player.blockPosition()) <= getDimension().x() / 2) {
            Vec3 randomPos =
                    ParticleUtils.randomPosInsideBox(player.getOnPos(), level.random, 0, 0, 0, 1.25, 2.25, 1.25);
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, randomPos.x, randomPos.y, randomPos.z, 5, .25, 0.25, .25,
                                0);
        }

        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        returnRune(level, pos);
        UUID playerUUID = context.getData(RitualDataTypes.PLAYER_TYPE).getPlayerUUID();
        Player player = level.getPlayerByUUID(playerUUID);
        if (player != null && player.isAlive() && pos.distManhattan(player.blockPosition()) <= getDimension().x() / 2) {
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, player.getOnPos(), MobSpawnType.TRIGGERED);
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, player.getOnPos(), MobSpawnType.TRIGGERED);
        }
    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        returnRune(level, pos);
        RitualUtils.getStartingPlayer(level, context).ifPresent(player -> {
            if (player.isAlive() && pos.distManhattan(player.blockPosition()) <= getDimension().x() / 2) {
                EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, player.getOnPos(), MobSpawnType.TRIGGERED);
                EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, player.getOnPos(), MobSpawnType.TRIGGERED);
            }
        });
    }

    public void returnRune(Level level, BlockPos pos) {
        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(), new ItemStack(
                MRItemInit.SOUL_CONTAINER_RUNE_ITEM.get())));
    }
}
