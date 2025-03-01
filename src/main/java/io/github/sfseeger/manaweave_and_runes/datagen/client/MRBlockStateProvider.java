package io.github.sfseeger.manaweave_and_runes.datagen.client;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blocks.RuneBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class MRBlockStateProvider extends BlockStateProvider {
    public MRBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ManaweaveAndRunes.MODID, existingFileHelper);
    }

    private void registerSimpleBlock(DeferredBlock<? extends Block> deferredBlock) {
        Block block = deferredBlock.get();
        this.simpleBlockWithItem(block, this.models().cubeAll(deferredBlock.getRegisteredName(), blockTexture(block)));
    }

    private void blockWithExistingModel(DeferredBlock<? extends Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(),
                            this.models().getExistingFile(ResourceLocation.parse(deferredBlock.getRegisteredName())));
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerSimpleBlock(MRBlockInit.TANZANITE_ORE);
        this.registerSimpleBlock(MRBlockInit.DEEPSLATE_TANZANITE_ORE);
        this.registerSimpleBlock(MRBlockInit.MANA_STORAGE_BLOCK);

        this.registerSimpleBlock(MRBlockInit.FIRE_MANA_INFUSED_ROCK_BLOCK);
        this.registerSimpleBlock(MRBlockInit.AIR_MANA_INFUSED_ROCK_BLOCK);
        this.registerSimpleBlock(MRBlockInit.EARTH_MANA_INFUSED_ROCK_BLOCK);
        this.registerSimpleBlock(MRBlockInit.WATER_MANA_INFUSED_ROCK_BLOCK);
        this.registerSimpleBlock(MRBlockInit.ENTROPY_MANA_INFUSED_ROCK_BLOCK);
        this.registerSimpleBlock(MRBlockInit.ORDER_MANA_INFUSED_ROCK_BLOCK);
        this.registerSimpleBlock(MRBlockInit.SOUL_MANA_INFUSED_ROCK_BLOCK);
        this.registerSimpleBlock(MRBlockInit.VOID_MANA_INFUSED_ROCK_BLOCK);

        blockWithExistingModel(MRBlockInit.RUNE_PEDESTAL_BLOCK);
        blockWithExistingModel(MRBlockInit.MANA_TRANSMITTER_BLOCK);
        blockWithExistingModel(MRBlockInit.MANA_COLLECTOR_BLOCK);
        blockWithExistingModel(MRBlockInit.RUNEWROUGHT_BENCH_BLOCK);
        blockWithExistingModel(MRBlockInit.SPELL_DESIGNER_BLOCK);

        simpleBlockItem(MRBlockInit.NOVICE_RITUAL_ANCHOR_BLOCK.get(), this.models()
                .getExistingFile(modLoc("block/ritual_anchor_java_model")));
        simpleBlockItem(MRBlockInit.MASTER_RITUAL_ANCHOR_BLOCK.get(), this.models()
                .getExistingFile(modLoc("block/ritual_anchor_java_model")));
        simpleBlockItem(MRBlockInit.ASCENDED_RITUAL_ANCHOR_BLOCK.get(), this.models()
                .getExistingFile(modLoc("block/ritual_anchor_java_model")));

        simpleBlockItem(MRBlockInit.NOVICE_MANA_CONCENTRATOR_BLOCK.get(), this.models()
                .getExistingFile(modLoc("item/mana_concentrator")));
        simpleBlockItem(MRBlockInit.MASTER_MANA_CONCENTRATOR_BLOCK.get(), this.models()
                .getExistingFile(modLoc("item/mana_concentrator")));
        simpleBlockItem(MRBlockInit.ASCENDED_MANA_CONCENTRATOR_BLOCK.get(), this.models()
                .getExistingFile(modLoc("item/mana_concentrator")));



        DeferredBlock<RuneBlock> deferredRuneBlock = MRBlockInit.RUNE_BLOCK;
        ResourceLocation runeBlockBase = this.blockTexture(deferredRuneBlock.get());
        ResourceLocation runeBlockActive = runeBlockBase.withSuffix("_active");
        ResourceLocation runeBlockInactive = runeBlockBase.withSuffix("_inactive");
        this.horizontalBlock(deferredRuneBlock.get(),
                             blockState -> blockState.getValue(RuneBlock.ACTIVE) ? this.models()
                                     .cubeAll(deferredRuneBlock.getRegisteredName() + "_active",
                                              runeBlockActive) : this.models()
                                     .cubeAll(deferredRuneBlock.getRegisteredName() + "_inactive", runeBlockInactive));
        this.simpleBlockItem(deferredRuneBlock.get(), this.models()
                .getExistingFile(ResourceLocation.parse(deferredRuneBlock.getRegisteredName() + "_inactive")));

        this.horizontalBlock(MRBlockInit.RUNE_CARVER_BLOCK.get(), modLoc("block/rune_carver_side"),
                             modLoc("block/rune_carver_front"), modLoc("block/rune_carver_top"));
        this.simpleBlockItem(MRBlockInit.RUNE_CARVER_BLOCK.get(),
                             this.models().getExistingFile(modLoc("block/rune_carver")));
        this.simpleBlockWithItem(MRBlockInit.TANZANITE_BLOCK.get(),
                                 this.models().cubeBottomTop(MRBlockInit.TANZANITE_BLOCK.getRegisteredName(),
                                                             modLoc("block/tanzanite_block_side"),
                                                             modLoc("block/tanzanite_block_bottom"),
                                                             modLoc("block/tanzanite_block_top")));
    }
}
