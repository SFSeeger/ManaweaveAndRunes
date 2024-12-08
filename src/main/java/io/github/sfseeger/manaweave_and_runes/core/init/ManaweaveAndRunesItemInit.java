package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ManaweaveAndRunesItemInit {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ManaweaveAndRunes.MODID);

    public static final DeferredItem<BlockItem> CRYSTAL_ORE_ITEM = ITEMS.registerSimpleBlockItem("crystal_ore", ManaweaveAndRunesBlockInit.CRYSTAL_ORE);
    public static final DeferredItem<BlockItem> MANA_STORAGE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("mana_storage_block", ManaweaveAndRunesBlockInit.MANA_STORAGE_BLOCK);
}