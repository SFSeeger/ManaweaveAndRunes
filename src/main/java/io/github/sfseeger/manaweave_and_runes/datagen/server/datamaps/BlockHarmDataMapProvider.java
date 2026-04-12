package io.github.sfseeger.manaweave_and_runes.datagen.server.datamaps;

import io.github.sfseeger.lib.common.datamaps.BlockHarmDataMap;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

public class BlockHarmDataMapProvider implements IDataMapRegistrar{
    @Override
    public void register(DataMapProvider provider) {
        DataMapProvider.Builder<BlockHarmDataMap, Block> harm_builder = provider.builder(BlockHarmDataMap.BLOCK_BLOCK_HARM_DATA);
        addBlockHarmData(harm_builder, Blocks.STONE, Blocks.COBBLESTONE, 0.0f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.COBBLESTONE, Blocks.GRAVEL, 0.0f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.GRAVEL, Blocks.SAND, 0.0f, 0.9f);
        addBlockHarmData(harm_builder, Blocks.SAND, Blocks.CLAY, 0.0f, 0.9f);
        addBlockHarmData(harm_builder, Blocks.CLAY, Blocks.SAND, 0.0f, 0.9f);

        addBlockHarmData(harm_builder, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS,
                         1.0f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.POLISHED_BLACKSTONE, Blocks.BLACKSTONE, 1.0f, 0.8f);

        addBlockHarmData(harm_builder, Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS, 1.5f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.POLISHED_DEEPSLATE, Blocks.DEEPSLATE, 1.5f, 0.8f);
        addBlockHarmData(harm_builder, Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE, 1.5f, 0.8f);
    }

    private void addBlockHarmData(DataMapProvider.Builder<BlockHarmDataMap, Block> builder, Block block, Block convertedBlock,
                                  float strength, float chance) {
        builder.add(block.builtInRegistryHolder(), new BlockHarmDataMap(convertedBlock, strength, chance), false);
    }
}
