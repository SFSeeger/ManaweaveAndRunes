package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerListRitualData;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.PLAYER_LIST_TYPE;

public class FlightRitual extends Ritual {
    public FlightRitual() {
        super(Tier.NOVICE, -1);
    }

    @Override
    public Vec3 getDimension() {
        return new Vec3(30, 30, 30);
    }

    @Override
    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {
        if (ticksPassed % 20 == 0) {
            Set<UUID> playerUUIDsInArea = new HashSet<>();
            level.getPlayers(p -> p.position().distanceTo(new Vec3(pos.getX(), pos.getY(), pos.getZ())) <= 30)
                    .forEach(player -> {
                        if (player.isAlive()) {
                            player.getAbilities().mayfly = true;
                            player.onUpdateAbilities();
                            PlayerListRitualData data = context.getData("affected_players", PLAYER_LIST_TYPE);
                            if (data == null) {
                                data = PlayerListRitualData.fromPlayerList(Set.of());
                                context.putData("affected_players", data);
                            }
                            data.addPlayer(player);
                            playerUUIDsInArea.add(player.getUUID());
                        }
                    });
            removeFlightFromPlayers(level, context, playerUUIDsInArea);
        }
        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        removeFlightFromPlayers(level, context, null);
    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        removeFlightFromPlayers(level, context, null);
    }

    private void removeFlightFromPlayers(Level level, RitualContext context, @Nullable Set<UUID> unaffectedPlayers) {
        PlayerListRitualData data = context.getData("affected_players", PLAYER_LIST_TYPE);
        if (data != null) {
            data.playerUUIDs()
                    .stream()
                    .filter(p -> unaffectedPlayers == null || !unaffectedPlayers.contains(p))
                    .forEach(player -> {
                        Player p = level.getServer().getPlayerList().getPlayer(player);
                        if (p != null) {
                            if (!p.isCreative() && !p.isSpectator()) {
                                p.getAbilities().mayfly = false;
                                p.onUpdateAbilities();
                                data.removePlayer(p);
                            }
                        }
                    });
        }
    }
}
