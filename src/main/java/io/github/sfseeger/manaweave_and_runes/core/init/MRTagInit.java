package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class MRTagInit {
    public static final TagKey<Block> RITUAL_MANA_PROVIDERS = BlockTags.create(ResourceLocation.fromNamespaceAndPath(
            ManaweaveAndRunes.MODID, "ritual_mana_provider"));
}
