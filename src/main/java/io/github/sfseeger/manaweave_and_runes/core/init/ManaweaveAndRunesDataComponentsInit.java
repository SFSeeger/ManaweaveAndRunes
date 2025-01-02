package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.mana.ManaDataComponent;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.data_components.BlockPosDataComponent;
import io.github.sfseeger.manaweave_and_runes.common.data_components.PlayerDataComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ManaweaveAndRunesDataComponentsInit {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ManaweaveAndRunes.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ManaDataComponent>> MANA_DATA_COMPONENT =
            DATA_COMPONENTS.registerComponentType(
                    "mana_data",
                    builder -> builder.persistent(ManaDataComponent.CODEC)
                            .networkSynchronized(ManaDataComponent.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPosDataComponent>>
            BLOCK_POS_DATA_COMPONENT =
            DATA_COMPONENTS.registerComponentType(
                    "block_pos_data",
                    builder -> builder.persistent(BlockPosDataComponent.CODEC)
                            .networkSynchronized(BlockPosDataComponent.STREAM_CODEC)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PlayerDataComponent>>
            PLAYER_DATA_COMPONENT =
            DATA_COMPONENTS.registerComponentType(
                    "player_data",
                    builder -> builder.persistent(PlayerDataComponent.CODEC)
                            .networkSynchronized(PlayerDataComponent.STREAM_CODEC)
            );
}
