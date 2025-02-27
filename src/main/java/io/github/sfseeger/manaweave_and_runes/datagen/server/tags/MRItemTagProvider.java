package io.github.sfseeger.manaweave_and_runes.datagen.server.tags;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRTagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MRItemTagProvider extends ItemTagsProvider {
    public MRItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ManaweaveAndRunes.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(MRTagInit.SPELL_MANA_PROVIDER)
                .add(MRItemInit.AMETHYST_FIRE_RUNE_ITEM.get())
                .add(MRItemInit.AMETHYST_AIR_RUNE_ITEM.get())
                .add(MRItemInit.RUNE_BRACELET_ITEM.get());

        this.tag(MRTagInit.MAGICAL_LOOT_ITEMS)
                .add(MRBlockInit.AIR_MANA_INFUSED_ROCK_BLOCK.asItem())
                .add(MRBlockInit.EARTH_MANA_INFUSED_ROCK_BLOCK.asItem())
                .add(MRBlockInit.FIRE_MANA_INFUSED_ROCK_BLOCK.asItem())
                .add(MRBlockInit.RUNE_BLOCK.asItem())
                .add(MRBlockInit.ENTROPY_MANA_INFUSED_ROCK_BLOCK.asItem())
                .add(MRBlockInit.ORDER_MANA_INFUSED_ROCK_BLOCK.asItem())
                .add(MRBlockInit.SOUL_MANA_INFUSED_ROCK_BLOCK.asItem())
                .add(MRBlockInit.VOID_MANA_INFUSED_ROCK_BLOCK.asItem())
        ;
    }
}
