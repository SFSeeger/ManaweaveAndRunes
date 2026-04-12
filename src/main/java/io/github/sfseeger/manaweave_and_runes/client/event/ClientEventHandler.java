package io.github.sfseeger.manaweave_and_runes.client.event;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.client.renderers.item.tooltip.ManaDisplayTooltipComponent;
import io.github.sfseeger.manaweave_and_runes.client.screens.gui.ManaweaversStaffGui;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.function.Function;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onGuiRenderPost(RenderGuiEvent.Post event){
        ManaweaversStaffGui.render(event);
    }
}
