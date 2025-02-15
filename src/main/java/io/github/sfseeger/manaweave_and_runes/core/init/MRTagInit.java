package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MRTagInit {
    public static final TagKey<Item> SPELL_MANA_PROVIDER = ItemTags.create(ResourceLocation.fromNamespaceAndPath(
            ManaweaveAndRunes.MODID, "spell_mana_provider"));
}
