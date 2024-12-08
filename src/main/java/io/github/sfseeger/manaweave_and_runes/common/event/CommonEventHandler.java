package io.github.sfseeger.manaweave_and_runes.common.event;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.api.mana.ManaRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus=EventBusSubscriber.Bus.MOD)
public class CommonEventHandler {
    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(ManaRegistry.MANA_REGISTRY);
    }
}
