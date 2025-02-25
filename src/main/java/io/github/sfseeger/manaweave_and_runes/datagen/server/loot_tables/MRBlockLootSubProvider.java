package io.github.sfseeger.manaweave_and_runes.datagen.server.loot_tables;

import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class MRBlockLootSubProvider extends BlockLootSubProvider {
    public MRBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected void generate() {
        this.add(MRBlockInit.TANZANITE_ORE.get(), (block) -> createOreDrop(block,
                                                                           MRItemInit.TANZANITE.get()));
        this.add(MRBlockInit.DEEPSLATE_TANZANITE_ORE.get(), (block) -> createOreDrop(block,
                                                                                     MRItemInit.TANZANITE.get()));
        this.dropSelf(MRBlockInit.TANZANITE_BLOCK.get());

        this.dropSelf(MRBlockInit.RUNE_BLOCK.get());
        this.dropSelf(MRBlockInit.FIRE_MANA_INFUSED_ROCK_BLOCK.get());
        this.dropSelf(MRBlockInit.AIR_MANA_INFUSED_ROCK_BLOCK.get());
        this.dropSelf(MRBlockInit.EARTH_MANA_INFUSED_ROCK_BLOCK.get());

        this.dropSelf(MRBlockInit.MANA_GENERATOR_BLOCK.get());
        this.dropSelf(MRBlockInit.MANA_COLLECTOR_BLOCK.get());
        this.dropSelf(MRBlockInit.MANA_STORAGE_BLOCK.get());
        this.dropSelf(MRBlockInit.RUNE_CARVER_BLOCK.get());
        this.dropSelf(MRBlockInit.RUNE_PEDESTAL_BLOCK.get());
        this.dropSelf(MRBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK.get());
        this.dropSelf(MRBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK.get());
        this.dropSelf(MRBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK.get());
        this.dropSelf(MRBlockInit.NOVICE_RITUAL_ANCHOR_BLOCK.get());
        this.dropSelf(MRBlockInit.MASTER_RITUAL_ANCHOR_BLOCK.get());
        this.dropSelf(MRBlockInit.ASCENDED_RITUAL_ANCHOR_BLOCK.get());
        this.dropSelf(MRBlockInit.MANA_TRANSMITTER_BLOCK.get());
        this.dropSelf(MRBlockInit.WAND_MODIFICATION_TABLE_BLOCK.get());
        this.dropSelf(MRBlockInit.SPELL_DESIGNER_BLOCK.get());

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return MRBlockInit.BLOCKS.getEntries().stream().map(e -> (Block) e.value()).toList();
    }
}
