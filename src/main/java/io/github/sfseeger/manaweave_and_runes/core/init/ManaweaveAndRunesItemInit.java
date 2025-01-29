package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.items.SpellHolderItem;
import io.github.sfseeger.lib.common.items.SpellPartHolderItem;
import io.github.sfseeger.lib.common.spells.SpellPart;
import io.github.sfseeger.lib.common.spells.data_components.SpellDataComponent;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit.SPELL_PART_DATA_COMPONENT;

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

    public static final DeferredItem<PositionRuneItem> POSITION_RUNE_ITEM = ITEMS.register("position_rune",
                                                                                           PositionRuneItem::new);
    public static final DeferredItem<SoulContainerRuneItem> SOUL_CONTAINER_RUNE_ITEM = ITEMS.register(
            "soul_container_rune", SoulContainerRuneItem::new);

    public static final DeferredItem<ManaConnector> MANA_CONNECTOR = ITEMS.register("mana_connector",
                                                                                    ManaConnector::new);

    public static final DeferredItem<ManaWeaversWandItem> MANA_WEAVERS_WAND_ITEM = ITEMS.register("mana_weavers_wand",
                                                                                                  ManaWeaversWandItem::new);


    public static final DeferredItem<SpellHolderItem> AMETHYST_SPELL_HOLDER_ITEM = ITEMS.register(
            "amethyst_spell_holder", () -> new SpellHolderItem(
                    new Item.Properties().component(ManaweaveAndRunesDataComponentsInit.SPELL_DATA_COMPONENT,
                                                    new SpellDataComponent(SpellHolderItem.s1))));

    // BLOCK ITEMS TODO: REPLACE WITH EVENT LISTENER
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

    public static final DeferredItem<BlockItem> NOVICE_RITUAL_ANCHOR_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("novice_ritual_anchor",
                                          ManaweaveAndRunesBlockInit.NOVICE_RITUAL_ANCHOR_BLOCK);
    public static final DeferredItem<BlockItem> MASTER_RITUAL_ANCHOR_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("master_ritual_anchor",
                                          ManaweaveAndRunesBlockInit.MASTER_RITUAL_ANCHOR_BLOCK);
    public static final DeferredItem<BlockItem> ASCENDED_RITUAL_ANCHOR_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("ascended_ritual_anchor",
                                          ManaweaveAndRunesBlockInit.ASCENDED_RITUAL_ANCHOR_BLOCK);

    public static final DeferredItem<BlockItem> MANA_TRANSMITTER_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("mana_transmitter",
                                          ManaweaveAndRunesBlockInit.MANA_TRANSMITTER_BLOCK);

    public static final DeferredItem<BlockItem> WAND_MODIFICATION_TABLE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("wand_modification_table",
                                          ManaweaveAndRunesBlockInit.WAND_MODIFICATION_TABLE_BLOCK);
    public static final DeferredItem<BlockItem> SPELL_DESIGNER_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("spell_designer", ManaweaveAndRunesBlockInit.SPELL_DESIGNER_BLOCK);


    // Spell Part Items
    public static final DeferredItem<SpellPartHolderItem> AMETHYST_SPELL_PART_ITEM = ITEMS.register("amethyst_spell_part",
            () -> new SpellPartHolderItem(new Item.Properties()));

    static {
        SpellNodeInit.SPELL_NODES.getEntries().forEach(entry -> {
            ITEMS.register(entry.getId().getPath(), () -> new SpellPartHolderItem(
                    new Item.Properties().component(SPELL_PART_DATA_COMPONENT, new SpellPart(entry))));
        });
    }
}