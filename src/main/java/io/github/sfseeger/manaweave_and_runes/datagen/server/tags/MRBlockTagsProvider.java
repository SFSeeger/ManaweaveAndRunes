package io.github.sfseeger.manaweave_and_runes.datagen.server.tags;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRTagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MRBlockTagsProvider extends BlockTagsProvider {
    public MRBlockTagsProvider(PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ManaweaveAndRunes.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MRBlockInit.MANA_COLLECTOR_BLOCK.get())
                .add(MRBlockInit.TANZANITE_ORE.get())
                .add(MRBlockInit.DEEPSLATE_TANZANITE_ORE.get())
                .add(MRBlockInit.MANA_STORAGE_BLOCK.get())
                .add(MRBlockInit.RUNE_BLOCK.get())
                .add(MRBlockInit.RUNE_PEDESTAL_BLOCK.get())
                .add(MRBlockInit.MANA_INFUSED_ROCK_BLOCK.get())
                .add(MRBlockInit.MANA_TRANSMITTER_BLOCK.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(MRBlockInit.RUNE_BLOCK.get())
                .add(MRBlockInit.MANA_INFUSED_ROCK_BLOCK.get())
                .add(MRBlockInit.RUNE_PEDESTAL_BLOCK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(MRBlockInit.MANA_COLLECTOR_BLOCK.get())
                .add(MRBlockInit.MANA_STORAGE_BLOCK.get())
                .add(MRBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK.get())
                .add(MRBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK.get())
                .add(MRBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK.get())
                .add(MRBlockInit.NOVICE_RITUAL_ANCHOR_BLOCK.get())
                .add(MRBlockInit.MASTER_RITUAL_ANCHOR_BLOCK.get())
                .add(MRBlockInit.ASCENDED_RITUAL_ANCHOR_BLOCK.get())
                .add(MRBlockInit.MANA_TRANSMITTER_BLOCK.get())
                .add(MRBlockInit.WAND_MODIFICATION_TABLE_BLOCK.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(MRBlockInit.TANZANITE_ORE.get())
                .add(MRBlockInit.DEEPSLATE_TANZANITE_ORE.get());

        this.tag(MRTagInit.MANA_INFUSED_BLOCK)
                .add(MRBlockInit.MANA_INFUSED_ROCK_BLOCK.get());
    }
}
