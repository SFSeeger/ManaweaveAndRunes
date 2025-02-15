package io.github.sfseeger.manaweave_and_runes.datagen.server.tags;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
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
                .add(ManaweaveAndRunesBlockInit.MANA_COLLECTOR_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.TANZANITE_ORE.get())
                .add(ManaweaveAndRunesBlockInit.DEEPSLATE_TANZANITE_ORE.get())
                .add(ManaweaveAndRunesBlockInit.MANA_STORAGE_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.RUNE_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.MANA_TRANSMITTER_BLOCK.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ManaweaveAndRunesBlockInit.RUNE_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ManaweaveAndRunesBlockInit.MANA_COLLECTOR_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.MANA_STORAGE_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.NOVICE_RITUAL_ANCHOR_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.MASTER_RITUAL_ANCHOR_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.ASCENDED_RITUAL_ANCHOR_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.MANA_TRANSMITTER_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.WAND_MODIFICATION_TABLE_BLOCK.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ManaweaveAndRunesBlockInit.TANZANITE_ORE.get())
                .add(ManaweaveAndRunesBlockInit.DEEPSLATE_TANZANITE_ORE.get());
    }
}
