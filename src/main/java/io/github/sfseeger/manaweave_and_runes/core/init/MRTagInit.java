package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class MRTagInit {
    public static final TagKey<Item> SPELL_MANA_PROVIDER =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "spell_mana_provider"));
    public static final TagKey<Item> MAGICAL_LOOT_ITEMS =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "magical_loot_items"));

    public static final TagKey<Item> CHISEL_ITEM = ItemTags.create(ManaweaveAndRunes.asResource("chisel_item"));

    public static final TagKey<Block> MANA_INFUSED_BLOCK =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana_infused_block"));
    public static final TagKey<Item> MANA_INFUSED_BLOCK_ITEM =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana_infused_block_item"));

    public static final TagKey<Biome> ENTROPY_STRUCTURE_BIOMES = TagKey.create(Registries.BIOME,
                                                                               ResourceLocation.fromNamespaceAndPath(
                                                                                       ManaweaveAndRunes.MODID,
                                                                                       "entropy_structure_biomes"));

}
