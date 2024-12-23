package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ManaweaveAndRunesBlockInit {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ManaweaveAndRunes.MODID);

    public static final DeferredBlock<Block> CRYSTAL_ORE = BLOCKS.registerSimpleBlock("crystal_ore",
                                                                                      BlockBehaviour.Properties.of()
                                                                                              .strength(3.0F)
                                                                                              .requiresCorrectToolForDrops()
                                                                                              .sound(SoundType.AMETHYST));
    public static final DeferredBlock<RuneBlock> RUNE_BLOCK = BLOCKS.register("rune_block", RuneBlock::new);
    public static final DeferredBlock<RunePedestalBlock> RUNE_PEDESTAL_BLOCK =
            BLOCKS.register("rune_pedestal", RunePedestalBlock::new);

    public static final DeferredBlock<Block> MANA_GENERATOR_BLOCK =
            BLOCKS.register("mana_generator", ManaGeneratorBlock::new);

    public static final DeferredBlock<Block> MANA_COLLECTOR_BLOCK =
            BLOCKS.register("mana_collector", ManaCollectorBlock::new);

    public static final DeferredBlock<Block> MANA_STORAGE_BLOCK =
            BLOCKS.register("mana_storage", ManaStorageBlock::new);

    public static final DeferredBlock<Block> RUNE_CARVER_BLOCK = BLOCKS.register("rune_carver", RuneCarverBlock::new);
}