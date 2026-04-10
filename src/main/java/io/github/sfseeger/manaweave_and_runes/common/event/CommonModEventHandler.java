package io.github.sfseeger.manaweave_and_runes.common.event;

import io.github.sfseeger.lib.common.datamaps.BlockHarmDataMap;
import io.github.sfseeger.lib.common.datamaps.BlockHealDataMap;
import io.github.sfseeger.lib.common.datamaps.ManaMapData;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.client.ClientPayloadHandler;
import io.github.sfseeger.manaweave_and_runes.common.ServerPayloadHandler;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRVillagers;
import io.github.sfseeger.manaweave_and_runes.core.payloads.CraftPayload;
import io.github.sfseeger.manaweave_and_runes.core.payloads.SwitchSpellPayload;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.List;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModEventHandler {
    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(ManaweaveAndRunesRegistries.MANA_REGISTRY);
        event.register(ManaweaveAndRunesRegistries.RITUAL_REGISTRY);
        event.register(ManaweaveAndRunesRegistries.RITUAL_DATA_TYPE_REGISTRY);
        event.register(ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY);
        event.register(ManaweaveAndRunesRegistries.MARK_REGISTRY);
    }

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playBidirectional(CraftPayload.TYPE, CraftPayload.STREAM_CODEC, new DirectionalPayloadHandler<>(
                ClientPayloadHandler::handleCraftPayload,
                ServerPayloadHandler::handleCraftPayload
        ));
        registrar.playBidirectional(SwitchSpellPayload.TYPE, SwitchSpellPayload.STREAM_CODEC,
                                    new DirectionalPayloadHandler<>(
                                            ClientPayloadHandler::handleSpellSwitchPayload,
                                            ServerPayloadHandler::handleSpellSwitchPayload
                                    ));
    }

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(ManaMapData.MANA_MAP_DATA);
        event.register(BlockHarmDataMap.BLOCK_BLOCK_HARM_DATA);
        event.register(BlockHealDataMap.BLOCK_BLOCK_HEAL_DATA);
    }
}
