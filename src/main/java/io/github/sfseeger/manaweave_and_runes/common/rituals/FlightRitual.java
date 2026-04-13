package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.context_data_types.builtin.PlayerListContextDataType;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static io.github.sfseeger.lib.common.context_data_types.ContextDataTypes.PLAYER_LIST_TYPE;

public class FlightRitual extends Ritual {
    public static final ResourceLocation FLIGHT_RITUAL_FLIGHT_MODIFIER_ID = ManaweaveAndRunes.asResource(
            "flight_ritual_flight_modifier");

    public FlightRitual() {
        super(Tier.MASTER, -1);
    }

    @Override
    public Vec3 getDimension() {
        return new Vec3(30, 30, 30);
    }

    @Override
    public RitualStepResult onRitualServerTick(RitualStateMachineContext ctx) {
        if (ctx.ticksPassed() % 20 == 0) {
            Set<UUID> playerUUIDsInArea = new HashSet<>();
            ((ServerLevel) ctx.level()).getPlayers(
                            p -> p.position().distanceTo(new Vec3(ctx.pos().getX(), ctx.pos().getY(), ctx.pos().getZ())) <= 30)
                    .forEach(player -> {
                        if (player.isAlive()) {
                            AttributeInstance attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
                            if (attribute != null) {
                                attribute.addOrUpdateTransientModifier(
                                        new AttributeModifier(FLIGHT_RITUAL_FLIGHT_MODIFIER_ID, 1.0,
                                                              AttributeModifier.Operation.ADD_VALUE));
                            }
                            PlayerListContextDataType data = ctx.contextMap()
                                    .getData("affected_players", PLAYER_LIST_TYPE).orElse(null);
                            if (data == null) {
                                data = PlayerListContextDataType.fromPlayerList(Set.of());
                                ctx.contextMap().putData("affected_players", data);
                            }
                            data.addPlayer(player);
                            playerUUIDsInArea.add(player.getUUID());
                        }
                    });
            removeFlightFromPlayers(ctx.level(), ctx.contextMap(), playerUUIDsInArea);
        }
        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(RitualStateMachineContext ctx) {
        removeFlightFromPlayers(ctx.level(), ctx.contextMap(), null);
    }

    @Override
    public void onRitualAbort(Level level, BlockPos pos, BlockState state, ContextMap context, RitualOriginType originType) {
        removeFlightFromPlayers(level, context, null);
    }

    private void removeFlightFromPlayers(Level level, ContextMap context, @Nullable Set<UUID> unaffectedPlayers) {
        context.getData("affected_players", PLAYER_LIST_TYPE).ifPresent(data -> data.playerUUIDs()
                .stream()
                .filter(p -> unaffectedPlayers == null || !unaffectedPlayers.contains(p))
                .forEach(player -> {
                    Player p = Objects.requireNonNull(level.getServer()).getPlayerList().getPlayer(player);
                    if (p != null) {
                        AttributeInstance attribute = p.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
                        if (attribute != null) {
                            attribute.removeModifier(FLIGHT_RITUAL_FLIGHT_MODIFIER_ID);
                        }
                    }
                }));
    }
}
