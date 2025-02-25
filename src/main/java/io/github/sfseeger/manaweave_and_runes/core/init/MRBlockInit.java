package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blocks.*;
import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.ManaConcentratorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.ManaConcentratorTypes;
import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.NoviceManaConcentratorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorTypes;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MRBlockInit {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ManaweaveAndRunes.MODID);

    public static final DeferredBlock<Block> TANZANITE_ORE = registerBlock("tanzanite_ore",
                                                                             () -> new DropExperienceBlock(
                                                                                     UniformInt.of(2, 8),
                                                                                     BlockBehaviour.Properties.of()
                                                                                             .strength(3.0F)
                                                                                             .requiresCorrectToolForDrops()
                                                                                             .sound(SoundType.STONE)));


    public static final DeferredBlock<Block> DEEPSLATE_TANZANITE_ORE = registerBlock("deepslate_tanzanite_ore",
                                                                                       () -> new DropExperienceBlock(
                                                                                               UniformInt.of(4, 8),
                                                                                               BlockBehaviour.Properties.of()
                                                                                                       .strength(4.5F,
                                                                                                                 3.0F)
                                                                                                       .requiresCorrectToolForDrops()
                                                                                                       .sound(SoundType.DEEPSLATE)));
    public static final DeferredBlock<Block> TANZANITE_BLOCK = registerBlock("tanzanite_block",
                                                                               () -> new Block(
                                                                                       BlockBehaviour.Properties.of()
                                                                                               .strength(3.0F)
                                                                                               .requiresCorrectToolForDrops()
                                                                                               .sound(SoundType.AMETHYST)));

    public static final DeferredBlock<RuneBlock> RUNE_BLOCK = registerBlock("rune_block", RuneBlock::new);

    public static final DeferredBlock<Block> FIRE_MANA_INFUSED_ROCK_BLOCK =
            registerBlock("fire_mana_infused_rock", ManaInfusedRock::new);
    public static final DeferredBlock<Block> AIR_MANA_INFUSED_ROCK_BLOCK =
            registerBlock("air_mana_infused_rock", ManaInfusedRock::new);
    public static final DeferredBlock<Block> EARTH_MANA_INFUSED_ROCK_BLOCK =
            registerBlock("earth_mana_infused_rock", ManaInfusedRock::new);


    public static final DeferredBlock<RunePedestalBlock> RUNE_PEDESTAL_BLOCK =
            registerBlock("rune_pedestal", RunePedestalBlock::new);

    public static final DeferredBlock<Block> MANA_GENERATOR_BLOCK =
            registerBlock("mana_generator", ManaGeneratorBlock::new);

    public static final DeferredBlock<Block> MANA_COLLECTOR_BLOCK =
            registerBlock("mana_collector", ManaCollectorBlock::new);

    public static final DeferredBlock<Block> MANA_STORAGE_BLOCK =
            registerBlock("mana_storage", ManaStorageBlock::new);

    public static final DeferredBlock<Block> RUNE_CARVER_BLOCK = registerBlock("rune_carver", RuneCarverBlock::new);

    public static final DeferredBlock<NoviceManaConcentratorBlock> NOVICE_MANA_CONCENTRATOR_BLOCK =
            registerBlock("novice_mana_concentrator", NoviceManaConcentratorBlock::new);
    public static final DeferredBlock<? extends ManaConcentratorBlock> MASTER_MANA_CONCENTRATOR_BLOCK =
            registerBlock("master_mana_concentrator", () -> new ManaConcentratorBlock(ManaConcentratorTypes.MASTER));
    public static final DeferredBlock<? extends ManaConcentratorBlock> ASCENDED_MANA_CONCENTRATOR_BLOCK =
            registerBlock("ascended_mana_concentrator",
                            () -> new ManaConcentratorBlock(ManaConcentratorTypes.ASCENDED));

    public static final DeferredBlock<RitualAnchorBlock> NOVICE_RITUAL_ANCHOR_BLOCK =
            BLOCKS.register("novice_ritual_anchor", () -> new RitualAnchorBlock(
                    RitualAnchorTypes.NOVICE));
    public static final DeferredBlock<RitualAnchorBlock> MASTER_RITUAL_ANCHOR_BLOCK =
            BLOCKS.register("master_ritual_anchor", () -> new RitualAnchorBlock(
                    RitualAnchorTypes.MASTER));
    public static final DeferredBlock<RitualAnchorBlock> ASCENDED_RITUAL_ANCHOR_BLOCK =
            BLOCKS.register("ascended_ritual_anchor", () -> new RitualAnchorBlock(
                    RitualAnchorTypes.ASCENDED));

    public static final DeferredBlock<ManaTransmitter> MANA_TRANSMITTER_BLOCK =
            registerBlock("mana_transmitter", ManaTransmitter::new);


    public static final DeferredBlock<WandModificationTableBlock> WAND_MODIFICATION_TABLE_BLOCK =
            registerBlock("wand_modification_table", WandModificationTableBlock::new);

    public static final DeferredBlock<SpellDesignerBlock> SPELL_DESIGNER_BLOCK =
            registerBlock("spell_designer", SpellDesignerBlock::new);


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> deferredBlock = BLOCKS.register(name, block);
        registerBlockItem(name, deferredBlock);
        return deferredBlock;
    }

    private static <T extends Block> DeferredItem<BlockItem> registerBlockItem(String name, Supplier<T> block) {
        return MRItemInit.ITEMS.registerSimpleBlockItem(name, block);
    }
}