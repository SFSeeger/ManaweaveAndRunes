package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ManaweaveAndRunesBlockTagsProvider extends BlockTagsProvider {
    public ManaweaveAndRunesBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ManaweaveAndRunesBlockInit.MANA_COLLECTOR_BLOCK.get())
                .add(ManaweaveAndRunesBlockInit.CRYSTAL_ORE.get())
                .add(ManaweaveAndRunesBlockInit.MANA_STORAGE_BLOCK.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ManaweaveAndRunesBlockInit.CRYSTAL_ORE.get());
    }
}