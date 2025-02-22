package io.github.sfseeger.manaweave_and_runes.datagen.server.loot_tables;

import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ManaweaveAndRunesBlockLootSubProvider extends BlockLootSubProvider {
    public ManaweaveAndRunesBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected void generate() {
        this.add(ManaweaveAndRunesBlockInit.CRYSTAL_ORE.get(), (block) -> createOreDrop(block,
                                                                                        ManaweaveAndRunesItemInit.CRYSTAL.get()));

        this.dropSelf(ManaweaveAndRunesBlockInit.RUNE_BLOCK.get());

        this.dropSelf(ManaweaveAndRunesBlockInit.MANA_GENERATOR_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.MANA_COLLECTOR_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.MANA_STORAGE_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.RUNE_CARVER_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.NOVICE_RITUAL_ANCHOR_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.MASTER_RITUAL_ANCHOR_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.ASCENDED_RITUAL_ANCHOR_BLOCK.get());
        this.dropSelf(ManaweaveAndRunesBlockInit.MANA_TRANSMITTER_BLOCK.get());

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ManaweaveAndRunesBlockInit.BLOCKS.getEntries().stream().map(e -> (Block) e.value()).toList();
    }
}
