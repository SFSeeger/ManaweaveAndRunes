package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.items.AirRuneItem;
import io.github.sfseeger.manaweave_and_runes.common.items.FireRuneItem;
import io.github.sfseeger.manaweave_and_runes.common.items.ManaDebugStickItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ManaweaveAndRunesItemInit {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ManaweaveAndRunes.MODID);

    public static final DeferredItem<Item> CRYSTAL = ITEMS.register("crystal",
                                                                    () -> new Item(new Item.Properties().rarity(
                                                                            Rarity.UNCOMMON)));
    public static final DeferredItem<Item> DIAMOND_CHISEL = ITEMS.register("diamond_chisel",
                                                                           () -> new Item(
                                                                                   new Item.Properties().durability(
                                                                                           1561).stacksTo(1)));


    public static final DeferredItem<Item> AMETHYST_BASE_RUNE = ITEMS.register("amethyst_base_rune",
                                                                               () -> new Item(
                                                                                       new Item.Properties().stacksTo(
                                                                                               1)));
    public static final DeferredItem<FireRuneItem> AMETHYST_FIRE_RUNE_ITEM = ITEMS.register("amethyst_fire_rune",
                                                                                            FireRuneItem::new);
    public static final DeferredItem<AirRuneItem> AMETHYST_AIR_RUNE_ITEM = ITEMS.register("amethyst_air_rune",
                                                                                          AirRuneItem::new);


    public static final DeferredItem<ManaDebugStickItem> MANA_DEBUG_STICK_ITEM = ITEMS.register("mana_debug_stick",
                                                                                                ManaDebugStickItem::new);

    public static final DeferredItem<BlockItem> CRYSTAL_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("crystal_ore", ManaweaveAndRunesBlockInit.CRYSTAL_ORE);
    public static final DeferredItem<BlockItem> RUNE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("rune_block", ManaweaveAndRunesBlockInit.RUNE_BLOCK);
    public static final DeferredItem<BlockItem> RUNE_PEDESTAL_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("rune_pedestal", ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK);


    public static final DeferredItem<BlockItem> MANA_GENERATOR_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("mana_generator", ManaweaveAndRunesBlockInit.MANA_GENERATOR_BLOCK);
    public static final DeferredItem<BlockItem> MANA_COLLECTOR_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("mana_collector", ManaweaveAndRunesBlockInit.MANA_COLLECTOR_BLOCK);
    public static final DeferredItem<BlockItem> MANA_STORAGE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("mana_storage", ManaweaveAndRunesBlockInit.MANA_STORAGE_BLOCK);
    public static final DeferredItem<BlockItem> RUNE_CARVER_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("rune_carver", ManaweaveAndRunesBlockInit.RUNE_CARVER_BLOCK);
    public static final DeferredItem<BlockItem> NOVICE_MANA_CONCENTRATOR_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("novice_mana_concentrator",
                                          ManaweaveAndRunesBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK);
    public static final DeferredItem<BlockItem> MASTER_MANA_CONCENTRATOR_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("master_mana_concentrator",
                                          ManaweaveAndRunesBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK);
    public static final DeferredItem<BlockItem> ASCENDED_MANA_CONCENTRATOR_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("ascended_mana_concentrator",
                                          ManaweaveAndRunesBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK);
}