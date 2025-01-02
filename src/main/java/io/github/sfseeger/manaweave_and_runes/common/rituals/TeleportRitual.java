package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import io.github.sfseeger.manaweave_and_runes.core.util.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {
        //TODO: This seems very expensive, consider refactoring
        if (ticksPassed % 10 == 0) {
            Tier tier = ((RitualAnchorBlock) state.getBlock()).ritualAnchorType.getTier();
            Player target;
            if (tier.greaterThanEqual(Tier.ASCENDED)) {
                PlayerRitualData playerRitualData = context.getData(PLAYER_TYPE);
                if (playerRitualData == null) {
                    return RitualStepResult.ABORT;
                }
                for (Player p : level.getServer().getPlayerList().getPlayers()) {
                    System.out.println(p.getName().getString());
                }
                target = level.getServer().getPlayerList().getPlayer(playerRitualData.getPlayerUUID());
            } else {
                target = getPlayer(level, context);
            }
            if (target == null) return RitualStepResult.ABORT;
            ServerLevel targetLevel = (ServerLevel) target.level();
            Vec3 vec =
                    ParticleUtils.randomPosInsideBox(BlockPos.containing(target.position()), level.getRandom(), -.25, 0,
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
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        Tier tier = ((RitualAnchorBlock) state.getBlock()).ritualAnchorType.getTier();
        RandomSource random = level.getRandom();
        Player target = null;
        if (tier.greaterThanEqual(Tier.ASCENDED)) {
            target = level.getServer().getPlayerList().getPlayer(context.getData(PLAYER_TYPE).getPlayerUUID());
        } else {
            target = getPlayer(level, context);
        }
        if (target != null) {
            target.teleportTo((ServerLevel) level, pos.getX() + random.nextInt(3) - 1, pos.getY(),
                              pos.getZ() + random.nextInt(3) - 1, Set.of(), 0, 0);
        }
        returnRune(level, pos);
    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        returnRune(level, pos);
        Player p = level.getServer()
                .getPlayerList()
                .getPlayer(context.getData("starting_player", PLAYER_TYPE).getPlayerUUID());
        if (p == null) return;
        p.hurt(p.damageSources().magic(), 5);
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
                ManaweaveAndRunesItemInit.SOUL_CONTAINER_RUNE_ITEM.get())));
    }
}
