package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ManaweaveAndRunesBlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ManaweaveAndRunes.MODID);

    public static final Supplier<BlockEntityType<ManaGeneratorBlockEntity>> MANA_GENERATOR_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register(
                    "mana_generator_block_entity",
                    () -> BlockEntityType.Builder.of(
                            ManaGeneratorBlockEntity::new,
                            ManaweaveAndRunesBlockInit.MANA_GENERATOR_BLOCK.get()
                    ).build(null)
            );

    public static final Supplier<BlockEntityType<ManaCollectorBlockEntity>> MANA_COLLECTOR_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register(
                    "mana_collector_block_entity",
                    () -> BlockEntityType.Builder.of(
                            ManaCollectorBlockEntity::new,
                            ManaweaveAndRunesBlockInit.MANA_COLLECTOR_BLOCK.get()
                    ).build(null)
            );
    public static final Supplier<BlockEntityType<ManaStorageBlockEntity>> MANA_STORAGE_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register(
                    "mana_storage_block_entity",
                    () -> BlockEntityType.Builder.of(
                            ManaStorageBlockEntity::new,
                            ManaweaveAndRunesBlockInit.MANA_STORAGE_BLOCK.get()
                    ).build(null)
            );
    public static final Supplier<BlockEntityType<RunePedestalBlockEntity>> RUNE_PEDESTAL_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register(
                    "rune_pedestal_block_entity",
                    () -> BlockEntityType.Builder.of(
                            RunePedestalBlockEntity::new,
                            ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get()
                    ).build(null)
            );

    public static final Supplier<BlockEntityType<ManaConcentratorBlockEntity>> MANA_CONCENTRATOR_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register(
                    "mana_concentrator_block_entity",
                    () -> BlockEntityType.Builder.of(
                            ManaConcentratorBlockEntity::new,
                            ManaweaveAndRunesBlockInit.Novice_MANA_CONCENTRATOR_BLOCK.get()
                    ).build(null)
            );
}