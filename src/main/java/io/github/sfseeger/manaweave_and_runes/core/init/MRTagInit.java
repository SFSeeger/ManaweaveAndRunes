package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MRTagInit {
    public static final TagKey<Item> SPELL_MANA_PROVIDER = ItemTags.create(ResourceLocation.fromNamespaceAndPath(
            ManaweaveAndRunes.MODID, "spell_mana_provider"));
    public static final TagKey<Block> MANA_INFUSED_BLOCK = BlockTags.create(ResourceLocation.fromNamespaceAndPath(
            ManaweaveAndRunes.MODID, "mana_infused_block"));
}
