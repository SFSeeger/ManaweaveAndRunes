package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.manaweave_and_runes.common.MRDamageTypes;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.util.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.PLAYER_TYPE;

public class TeleportRitual extends Ritual {
    public TeleportRitual() {
        super(Tier.MASTER, 3 * 20);
    }

    @Override
    public Vec3 getDimension() {
        return null;
    }

    @Override
    public RitualStepResult onRitualServerTick(RitualStateMachineContext ctx) {
        //TODO: This seems very expensive, consider refactoring
        if (ctx.ticksPassed() % 10 == 0) {
            Tier tier = ((RitualAnchorBlock) ctx.state().getBlock()).ritualAnchorType.getTier();
            Player target;
            if (tier.greaterThanEqual(Tier.ASCENDED)) {
                PlayerRitualData playerRitualData = ctx.ritualContext().getData(PLAYER_TYPE);
                if (playerRitualData == null) {
                    return RitualStepResult.FAIL;
                }
                target = ctx.level().getServer().getPlayerList().getPlayer(playerRitualData.getPlayerUUID());
            } else {
                target = getPlayer(ctx.level(), ctx.ritualContext());
            }
            if (target == null) return RitualStepResult.FAIL;
            ServerLevel targetLevel = (ServerLevel) target.level();
            Vec3 vec =
                    ParticleUtils.randomPosInsideBox(BlockPos.containing(target.position()), ctx.level().getRandom(), -.25, 0,
                                                     -.25, 1.25, 1.25, 1.25);
            targetLevel.sendParticles(ParticleTypes.PORTAL, vec.x, vec.y, vec.z, 15, .5, 0, .5, 1);
        }
        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualClientTick(Level level, BlockPos pos, BlockState state, int ticksPassed, RitualContext context,
            RitualOriginType originType) {
        for (int i = 0; i < 40; i++) {
            Vec3 vec = ParticleUtils.randomPosInsideBox(pos, level.getRandom(), -5, 0, -5, 5, 3, 5);
            level.addParticle(ParticleTypes.PORTAL, vec.x, vec.y, vec.z, 1, 1, 1);
        }
    }

    @Override
    public void onRitualEnd(RitualStateMachineContext ctx) {
        Tier tier = ((RitualAnchorBlock) ctx.state().getBlock()).ritualAnchorType.getTier();
        RandomSource random = ctx.level().getRandom();
        Player target;
        if (tier.greaterThanEqual(Tier.ASCENDED)) {
            target = ctx.level().getServer().getPlayerList().getPlayer(ctx.ritualContext().getData(PLAYER_TYPE).getPlayerUUID());
        } else {
            target = getPlayer(ctx.level(), ctx.ritualContext());
        }
        if (target != null) {
            target.teleportTo((ServerLevel) ctx.level(), ctx.pos().getX() + random.nextInt(3) - 1, ctx.pos().getY(),
                              ctx.pos().getZ() + random.nextInt(3) - 1, Set.of(), 0, 0);
        }
        returnRune(ctx.level(), ctx.pos());
    }

    @Override
    public void onRitualAbort(Level level, BlockPos pos, BlockState state, RitualContext context,
                              RitualOriginType originType) {
        returnRune(level, pos);
        Player p = level.getServer()
                .getPlayerList()
                .getPlayer(context.getData("starting_player", PLAYER_TYPE).getPlayerUUID());
        if (p == null) return;
        p.hurt(new DamageSource(level.registryAccess()
                                        .registryOrThrow(Registries.DAMAGE_TYPE)
                                        .getHolderOrThrow(MRDamageTypes.RITUAL_FAILURE)), 5);

        p.hurt(MRDamageTypes.createRitualFailure(level, pos), 5);
        // TODO: Add sparks or something
    }

    public Player getPlayer(Level level, RitualContext context) {
        PlayerRitualData playerRitualData = context.getData(PLAYER_TYPE);
        if (playerRitualData == null) {
            return null;
        }
        return level.getPlayerByUUID(playerRitualData.getPlayerUUID());
    }

    public void returnRune(Level level, BlockPos pos) {
        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(
                MRItemInit.SOUL_CONTAINER_RUNE_ITEM.get())));
    }
}
