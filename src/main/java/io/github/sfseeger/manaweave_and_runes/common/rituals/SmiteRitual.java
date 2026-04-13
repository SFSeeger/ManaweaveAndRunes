package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.context_data_types.builtin.PlayerContextDataType;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.context_data_types.ContextDataTypes;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
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

import java.util.Optional;
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
    public RitualStepResult onRitualServerTick(RitualStateMachineContext ctx) {
        Optional<PlayerContextDataType> playerData = ctx.contextMap().getData(ContextDataTypes.PLAYER_TYPE);
        if (playerData.isEmpty()) return RitualStepResult.FAIL;
        UUID playerUUID = playerData.get().getPlayerUUID();

        Player player = ctx.level().getPlayerByUUID(playerUUID);
        if (player != null && player.isAlive() && ctx.pos()
                .distManhattan(player.blockPosition()) <= getDimension().x() / 2) {
            Vec3 randomPos =
                    ParticleUtils.randomPosInsideBox(player.getOnPos(), ctx.level().random, 0, 0, 0, 1.25, 2.25, 1.25);
            ((ServerLevel) ctx.level())
                    .sendParticles(ParticleTypes.ELECTRIC_SPARK, randomPos.x, randomPos.y, randomPos.z, 5, .25, 0.25,
                                   .25,
                                   0);
        }

        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(RitualStateMachineContext ctx) {
        returnRune(ctx.level(), ctx.pos());
        Optional<PlayerContextDataType> playerData = ctx.contextMap().getData(ContextDataTypes.PLAYER_TYPE);
        if (playerData.isEmpty()) return;
        UUID playerUUID = playerData.get().getPlayerUUID();
        Player player = ctx.level().getPlayerByUUID(playerUUID);
        if (player != null && player.isAlive() && ctx.pos()
                .distManhattan(player.blockPosition()) <= getDimension().x() / 2) {
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) ctx.level(), player.getOnPos(), MobSpawnType.TRIGGERED);
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) ctx.level(), player.getOnPos(), MobSpawnType.TRIGGERED);
        }
    }

    @Override
    public void onRitualAbort(Level level, BlockPos pos, BlockState state, ContextMap context,
                              RitualOriginType originType
    ) {
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
