package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.items.AbstractRuneItem;
import io.github.sfseeger.lib.common.items.RuneCarvingTemplate;
import io.github.sfseeger.lib.common.items.SpellHolderItem;
import io.github.sfseeger.lib.common.items.SpellPartHolderItem;
import io.github.sfseeger.lib.common.spells.Spell;
import io.github.sfseeger.lib.common.spells.SpellPart;
import io.github.sfseeger.lib.common.spells.data_components.SpellDataComponent;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.items.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit.SPELL_PART_DATA_COMPONENT;

public class MRItemInit {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ManaweaveAndRunes.MODID);

    public static final DeferredItem<Item> TANZANITE = ITEMS.register("tanzanite",
                                                                      () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIAMOND_CHISEL = ITEMS.register("diamond_chisel",
                                                                           () -> new Item(
                                                                                   new Item.Properties().durability(
                                                                                           1561).stacksTo(1)));
    public static final DeferredItem<Item> MANA_WEAVER_WAND_ITEM = ITEMS.register("mana_weaver_wand",
                                                                                  () -> new Item(
                                                                                     new Item.Properties().stacksTo(
                                                                                             1)));


    public static final DeferredItem<Item> AMETHYST_BASE_RUNE = ITEMS.register("amethyst_base_rune",
                                                                               () -> new Item(
                                                                                       new Item.Properties().stacksTo(
                                                                                               1)));
    public static final DeferredItem<FireRuneItem> AMETHYST_FIRE_RUNE_ITEM = ITEMS.register("amethyst_fire_rune",
                                                                                            FireRuneItem::new);
    public static final DeferredItem<AirRuneItem> AMETHYST_AIR_RUNE_ITEM = ITEMS.register("amethyst_air_rune",
                                                                                          AirRuneItem::new);
    public static final DeferredItem<AbstractRuneItem> AMETHYST_EARTH_RUNE_ITEM = ITEMS.register(
            "amethyst_earth_rune", () -> new AbstractRuneItem(ManaInit.EARTH_MANA));
    public static final DeferredItem<AbstractRuneItem> AMETHYST_WATER_RUNE_ITEM = ITEMS.register(
            "amethyst_water_rune", () -> new AbstractRuneItem(ManaInit.WATER_MANA));
    public static final DeferredItem<AbstractRuneItem> AMETHYST_VOID_RUNE_ITEM = ITEMS.register(
            "amethyst_void_rune", () -> new AbstractRuneItem(ManaInit.VOID_MANA));
    public static final DeferredItem<AbstractRuneItem> AMETHYST_SOUL_RUNE_ITEM = ITEMS.register(
            "amethyst_soul_rune", () -> new AbstractRuneItem(ManaInit.SOUL_MANA));
    public static final DeferredItem<AbstractRuneItem> AMETHYST_ORDER_RUNE_ITEM = ITEMS.register(
            "amethyst_order_rune", () -> new AbstractRuneItem(ManaInit.ORDER_MANA));
    public static final DeferredItem<AbstractRuneItem> AMETHYST_ENTROPY_RUNE_ITEM = ITEMS.register(
            "amethyst_entropy_rune", () -> new AbstractRuneItem(ManaInit.ENTROPY_MANA));


    public static final DeferredItem<RuneCarvingTemplate> AIR_RUNE_CARVING_TEMPLATE = ITEMS.register(
            "air_rune_carving_template", () -> new RuneCarvingTemplate(new Item.Properties().rarity(Rarity.COMMON)));
    public static final DeferredItem<RuneCarvingTemplate> FIRE_RUNE_CARVING_TEMPLATE = ITEMS.register(
            "fire_rune_carving_template", () -> new RuneCarvingTemplate(new Item.Properties().rarity(Rarity.COMMON)));
    public static final DeferredItem<RuneCarvingTemplate> EARTH_RUNE_CARVING_TEMPLATE = ITEMS.register(
            "earth_rune_carving_template", () -> new RuneCarvingTemplate(new Item.Properties().rarity(Rarity.COMMON)));
    public static final DeferredItem<RuneCarvingTemplate> WATER_RUNE_CARVING_TEMPLATE = ITEMS.register(
            "water_rune_carving_template", () -> new RuneCarvingTemplate(new Item.Properties().rarity(Rarity.COMMON)));
    public static final DeferredItem<RuneCarvingTemplate> VOID_RUNE_CARVING_TEMPLATE = ITEMS.register(
            "void_rune_carving_template", () -> new RuneCarvingTemplate(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<RuneCarvingTemplate> SOUL_RUNE_CARVING_TEMPLATE = ITEMS.register(
            "soul_rune_carving_template", () -> new RuneCarvingTemplate(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<RuneCarvingTemplate> ORDER_RUNE_CARVING_TEMPLATE = ITEMS.register(
            "order_rune_carving_template",
            () -> new RuneCarvingTemplate(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<RuneCarvingTemplate> ENTROPY_RUNE_CARVING_TEMPLATE = ITEMS.register(
            "entropy_rune_carving_template",
            () -> new RuneCarvingTemplate(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<RuneCarvingTemplate> RUNE_BLOCK_CARVING_TEMPLATE = ITEMS.register(
            "rune_block_carving_template", () -> new RuneCarvingTemplate(new Item.Properties().rarity(Rarity.COMMON)));


    public static final DeferredItem<RuneBraceletItem> RUNE_BRACELET_ITEM = ITEMS.register("rune_bracelet",
                                                                                           RuneBraceletItem::new);


    public static final DeferredItem<ManaDebugStickItem> MANA_DEBUG_STICK_ITEM = ITEMS.register("mana_debug_stick",
                                                                                                ManaDebugStickItem::new);

    public static final DeferredItem<PositionRuneItem> POSITION_RUNE_ITEM = ITEMS.register("position_rune",
                                                                                           PositionRuneItem::new);
    public static final DeferredItem<SoulContainerRuneItem> SOUL_CONTAINER_RUNE_ITEM = ITEMS.register(
            "soul_container_rune", SoulContainerRuneItem::new);

    public static final DeferredItem<ManaConnector> MANA_CONNECTOR = ITEMS.register("mana_connector",
                                                                                    ManaConnector::new);

    public static final DeferredItem<ManaWeaversStaffItem> MANA_WEAVERS_STAFF_ITEM =
            ITEMS.register("mana_weavers_staff",
                           ManaWeaversStaffItem::new);


    public static final DeferredItem<SpellHolderItem> SPELL_HOLDER_ITEM = ITEMS.register(
            "spell_holder", () -> new SpellHolderItem(
                    new Item.Properties().component(MRDataComponentsInit.SPELL_DATA_COMPONENT,
                                                    new SpellDataComponent(new Spell()))));

    // Spell Part Items
    public static final DeferredItem<SpellPartHolderItem> SPELL_PART = ITEMS.register("spell_part",
                                                                                      () -> new SpellPartHolderItem(
                                                                                              new Item.Properties()));

    //Custom Block Items
    public static final DeferredItem<ManaConcentratorBlockItem> NOVICE_MANA_CONCENTRATOR_BLOCK_ITEM = ITEMS.register(
            "novice_mana_concentrator",
            () -> new ManaConcentratorBlockItem(MRBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK.get()));
    public static final DeferredItem<ManaConcentratorBlockItem> MASTER_MANA_CONCENTRATOR_BLOCK_ITEM = ITEMS.register(
            "master_mana_concentrator",
            () -> new ManaConcentratorBlockItem(MRBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK.get()));
    public static final DeferredItem<ManaConcentratorBlockItem> ASCENDED_MANA_CONCENTRATOR_BLOCK_ITEM = ITEMS.register(
            "ascended_mana_concentrator",
            () -> new ManaConcentratorBlockItem(MRBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK.get()));

    public static final DeferredItem<RitualAnchorBlockItem> NOVICE_RITUAL_ANCHOR_BLOCK_ITEM = ITEMS.register(
            "novice_ritual_anchor",
            () -> new RitualAnchorBlockItem(MRBlockInit.NOVICE_RITUAL_ANCHOR_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<RitualAnchorBlockItem> MASTER_RITUAL_ANCHOR_BLOCK_ITEM = ITEMS.register(
            "master_ritual_anchor",
            () -> new RitualAnchorBlockItem(MRBlockInit.MASTER_RITUAL_ANCHOR_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<RitualAnchorBlockItem> ASCENDED_RITUAL_ANCHOR_BLOCK_ITEM = ITEMS.register(
            "ascended_ritual_anchor",
            () -> new RitualAnchorBlockItem(MRBlockInit.ASCENDED_RITUAL_ANCHOR_BLOCK.get(), new Item.Properties()));

    static {
        SpellNodeInit.SPELL_NODES.getEntries()
                .forEach(entry -> ITEMS.register(entry.getId().getPath(), () -> new SpellPartHolderItem(
                        new Item.Properties().component(SPELL_PART_DATA_COMPONENT, new SpellPart(entry)))));

    }
}