package io.github.sfseeger.manaweave_and_runes.common.event;

import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataAttachmentInit;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static io.github.sfseeger.manaweave_and_runes.common.rituals.FlightRitual.FLIGHT_RITUAL_FLIGHT_MODIFIER_ID;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEventHandler {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        AttributeInstance attribute = event.getEntity().getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (attribute != null) {
            attribute.removeModifier(FLIGHT_RITUAL_FLIGHT_MODIFIER_ID);
        }
    }

    @SubscribeEvent
    public static void onPlayerPreTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        for (MarkInstance markInstance : player.getData(MRDataAttachmentInit.MARKS_DATA_ATTACHMENT_TYPE)
                .getMarks()) {
            markInstance.applyEffect(player);
        }
    }
}
