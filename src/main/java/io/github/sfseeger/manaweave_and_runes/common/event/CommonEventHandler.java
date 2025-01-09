package io.github.sfseeger.manaweave_and_runes.common.event;

import io.github.sfseeger.lib.common.mana.ManaHelper;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonEventHandler {
    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(ManaweaveAndRunesRegistries.MANA_REGISTRY);
        event.register(ManaweaveAndRunesRegistries.RITUAL_REGISTRY);
        event.register(ManaweaveAndRunesRegistries.RITUAL_DATA_TYPE_REGISTRY);
    }

    @SubscribeEvent
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        ManaHelper.setItemMana(event, Items.COAL, Manas.FireMana, 50);
        ManaHelper.setItemMana(event, Items.BLUE_DYE, Manas.AirMana, 20);
    }
}
