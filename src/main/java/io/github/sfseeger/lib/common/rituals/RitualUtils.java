package io.github.sfseeger.lib.common.rituals;

import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.context_data_types.builtin.PlayerContextDataType;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;

import static io.github.sfseeger.lib.common.context_data_types.ContextDataTypes.PLAYER_TYPE;

public class RitualUtils {
    public static void displayMessageToStartingPlayer(Component message, Level level, ContextMap context) {
        getStartingPlayer(level, context).ifPresent(player -> player.displayClientMessage(message, false));
    }

    public static void displayMessageToStartingPlayer(Component message, RitualStateMachineContext ctx) {
        displayMessageToStartingPlayer(message, ctx.level(), ctx.contextMap());
    }

    public static Optional<Player> getStartingPlayer(Level level, ContextMap context) {
        PlayerContextDataType starting_player = context.getData("starting_player", PLAYER_TYPE).orElse(null);
        if (starting_player != null)
            return Optional.ofNullable(level.getPlayerByUUID(starting_player.getPlayerUUID()));
        return Optional.empty();
    }

    public static Optional<Player> getStartingPlayer(RitualStateMachineContext ctx) {
        return getStartingPlayer(ctx.level(), ctx.contextMap());
    }
}
