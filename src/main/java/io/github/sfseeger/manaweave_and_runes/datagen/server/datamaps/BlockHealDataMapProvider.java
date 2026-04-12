package io.github.sfseeger.manaweave_and_runes.datagen.server.datamaps;

import io.github.sfseeger.lib.common.datamaps.BlockHealDataMap;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

public class BlockHealDataMapProvider implements IDataMapRegistrar {
    @Override
    public void register(DataMapProvider provider) {
        DataMapProvider.Builder<BlockHealDataMap, Block> heal_builder =
                provider.builder(BlockHealDataMap.BLOCK_BLOCK_HEAL_DATA);
        addBlockHealData(heal_builder, Blocks.COBBLESTONE, Blocks.STONE, 0.0f, 0.8f);
        addBlockHealData(heal_builder, Blocks.GRAVEL, Blocks.COBBLESTONE, 0.0f, 0.8f);
        addBlockHealData(heal_builder, Blocks.SAND, Blocks.GRAVEL, 0.0f, 0.9f);
        addBlockHealData(heal_builder, Blocks.CLAY, Blocks.SAND, 0.0f, 0.9f);

        addBlockHealData(heal_builder, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS,
                         1.0f, 0.8f);
        addBlockHealData(heal_builder, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, 1.0f, 0.8f);

        addBlockHealData(heal_builder, Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICKS, 1.5f, 0.8f);
        addBlockHealData(heal_builder, Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE, 1.5f, 0.8f);
        addBlockHealData(heal_builder, Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE, 1.5f, 0.8f);
    }

    private void addBlockHealData(DataMapProvider.Builder<BlockHealDataMap, Block> builder, Block block, Block convertedBlock,
                                  float strength, float chance
    ) {
        builder.add(block.builtInRegistryHolder(), new BlockHealDataMap(convertedBlock, strength, chance), false);
    }
}
