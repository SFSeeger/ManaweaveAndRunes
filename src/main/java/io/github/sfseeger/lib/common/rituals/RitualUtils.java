package io.github.sfseeger.lib.common.rituals;

import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.PLAYER_TYPE;

public class RitualUtils {
    public static void displayMessageToStartingPlayer(Component message, Level level, RitualContext context) {
        PlayerRitualData starting_player = context.getData("starting_player", PLAYER_TYPE);
        if (starting_player != null) {
            Player player = level.getPlayerByUUID(starting_player.getPlayerUUID());
            if (player != null) {
                player.displayClientMessage(message, false);
            }
        }
    }
}
