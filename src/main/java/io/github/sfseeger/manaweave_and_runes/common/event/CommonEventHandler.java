package io.github.sfseeger.manaweave_and_runes.common.event;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEventHandler {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        //TODO: This is an ugly fix to ensure no player can cheese the flight ritual by logging out and back in
        // We should probably store a list somewhere with affected players and check that list on login
        if (event.getEntity() instanceof ServerPlayer player && !player.isCreative() && !player.isSpectator()) {
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
        }
    }
}
