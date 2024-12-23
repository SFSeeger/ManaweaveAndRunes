package io.github.sfseeger.manaweave_and_runes.datagen.client;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blocks.RuneBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ManaweaveAndRunesBlockStateProvider extends BlockStateProvider {
    public ManaweaveAndRunesBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ManaweaveAndRunes.MODID, existingFileHelper);
    }

    private void registerSimpleBlock(DeferredBlock<? extends Block> deferredBlock) {
        Block block = deferredBlock.get();
        this.simpleBlockWithItem(block, this.models().cubeAll(deferredBlock.getRegisteredName(), blockTexture(block)));
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerSimpleBlock(ManaweaveAndRunesBlockInit.CRYSTAL_ORE);
        this.registerSimpleBlock(ManaweaveAndRunesBlockInit.MANA_COLLECTOR_BLOCK);
        DeferredBlock<RuneBlock> deferredRuneBlock = ManaweaveAndRunesBlockInit.RUNE_BLOCK;
        ResourceLocation runeBlockBase = this.blockTexture(deferredRuneBlock.get());
        ResourceLocation runeBlockActive = runeBlockBase.withSuffix("_active");
        ResourceLocation runeBlockInactive = runeBlockBase.withSuffix("_inactive");
        this.horizontalBlock(deferredRuneBlock.get(), blockState -> blockState.getValue(
                RuneBlock.ACTIVE) ? this.models()
                .cubeAll(deferredRuneBlock.getRegisteredName() + "_active", runeBlockActive) : this.models()
                .cubeAll(deferredRuneBlock.getRegisteredName() + "_inactive", runeBlockInactive));
        this.simpleBlockItem(deferredRuneBlock.get(), this.models().getExistingFile(
                ResourceLocation.parse(deferredRuneBlock.getRegisteredName() + "_inactive")));

        this.horizontalBlock(ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get(), this.models().getExistingFile(
                ResourceLocation.parse(ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.getRegisteredName())));
        this.simpleBlockItem(ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get(), this.models().getExistingFile(
                ResourceLocation.parse(ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.getRegisteredName())));
    }
}
