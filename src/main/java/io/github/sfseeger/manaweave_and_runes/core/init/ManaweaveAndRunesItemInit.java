package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.items.AirRuneItem;
import io.github.sfseeger.manaweave_and_runes.common.items.FireRuneItem;
import io.github.sfseeger.manaweave_and_runes.common.items.ManaDebugStickItem;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ManaweaveAndRunesItemInit {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ManaweaveAndRunes.MODID);

    public static final DeferredItem<ManaDebugStickItem> MANA_DEBUG_STICK_ITEM = ITEMS.register("mana_debug_stick",
                                                                                                ManaDebugStickItem::new);
    public static final DeferredItem<FireRuneItem> AMETHYST_FIRE_RUNE_ITEM = ITEMS.register("amethyst_fire_rune",
                                                                                            FireRuneItem::new);
    public static final DeferredItem<AirRuneItem> AMETHYST_AIR_RUNE_ITEM = ITEMS.register("amethyst_air_rune",
                                                                                          AirRuneItem::new);


    public static final DeferredItem<BlockItem> CRYSTAL_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("crystal_ore", ManaweaveAndRunesBlockInit.CRYSTAL_ORE);
    public static final DeferredItem<BlockItem> MANA_STORAGE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("mana_storage_block", ManaweaveAndRunesBlockInit.MANA_STORAGE_BLOCK);
    public static final DeferredItem<BlockItem> MANA_COLLECTOR_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("mana_collector_block", ManaweaveAndRunesBlockInit.MANA_COLLECTOR_BLOCK);
}