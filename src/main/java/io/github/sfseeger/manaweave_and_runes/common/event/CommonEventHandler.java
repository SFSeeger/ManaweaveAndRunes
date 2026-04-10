package io.github.sfseeger.manaweave_and_runes.common.event;

import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataAttachmentInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRVillagers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.List;

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

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event){
        if (event.getType() == MRVillagers.RUNE_SMITH.value()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(MRItemInit.AMETHYST_AIR_RUNE_ITEM.get(), 1), 6, 3, 0.05f
            ));
            trades.get(1).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(MRItemInit.AMETHYST_EARTH_RUNE_ITEM.get(), 1), 6, 3, 0.05f
            ));
            trades.get(1).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(MRItemInit.AMETHYST_FIRE_RUNE_ITEM.get(), 1), 6, 3, 0.05f
            ));
            trades.get(1).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(MRItemInit.AMETHYST_WATER_RUNE_ITEM.get(), 1), 6, 3, 0.05f
            ));
            trades.get(1).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.AMETHYST_SHARD, 10),
                    new ItemStack(Items.EMERALD, 2), 12, 3, 0.05f
            ));

            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 10),
                    new ItemStack(MRItemInit.TANZANITE.get(), 1), 3, 6, 0.05f
            ));
            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemCost(MRItemInit.TANZANITE, 1),
                    new ItemStack(Items.EMERALD, 8), 3, 6, 0.05f
            ));
            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 20),
                    new ItemStack(MRItemInit.AIR_RUNE_CARVING_TEMPLATE.get(), 1), 3, 6, 0.05f
            ));
            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 20),
                    new ItemStack(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE.get(), 1), 3, 6, 0.05f
            ));
            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 20),
                    new ItemStack(MRItemInit.WATER_RUNE_CARVING_TEMPLATE.get(), 1), 3, 6, 0.05f
            ));
            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 20),
                    new ItemStack(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE.get(), 1), 3, 6, 0.05f
            ));
            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2),
                    new ItemStack(MRItemInit.POSITION_RUNE_ITEM.get(), 1), 3, 6, 0.05f
            ));
        }
    }

    @SubscribeEvent
    public static void addWanderingTraderTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        rareTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 30),
                new ItemStack(MRItemInit.ENTROPY_RUNE_CARVING_TEMPLATE.get(), 1), 1, 10, 0.1f
        ));
    }
}
