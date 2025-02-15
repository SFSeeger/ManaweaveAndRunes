package io.github.sfseeger.manaweave_and_runes.datagen.server.tags;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRTagInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
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
                .add(ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM.get())
                .add(ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM.get());
    }
}
