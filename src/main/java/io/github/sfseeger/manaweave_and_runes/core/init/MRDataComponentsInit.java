package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.mana.ManaDataComponent;
import io.github.sfseeger.lib.common.spells.SpellPart;
import io.github.sfseeger.lib.common.spells.data_components.SpellDataComponent;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.data_components.BlockPosDataComponent;
import io.github.sfseeger.manaweave_and_runes.common.data_components.ItemStackHandlerDataComponent;
import io.github.sfseeger.manaweave_and_runes.common.data_components.PlayerDataComponent;
import io.github.sfseeger.manaweave_and_runes.common.data_components.SelectedSlotDataComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MRDataComponentsInit {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ManaweaveAndRunes.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ManaDataComponent>> MANA_DATA_COMPONENT =
            DATA_COMPONENTS.registerComponentType(
                    "mana_data",
                    builder -> builder.persistent(ManaDataComponent.CODEC)
                            .networkSynchronized(ManaDataComponent.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellDataComponent>>
            SPELL_DATA_COMPONENT =
            DATA_COMPONENTS.registerComponentType(
                    "spell_data",
                    builder -> builder.persistent(SpellDataComponent.CODEC)
                            .networkSynchronized(SpellDataComponent.STREAM_CODEC)
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

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellPart>> SPELL_PART_DATA_COMPONENT =
            DATA_COMPONENTS.registerComponentType(
                    "spell_part",
                    builder -> builder.persistent(SpellPart.CODEC)
                            .networkSynchronized(SpellPart.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackHandlerDataComponent>>
            ITEM_STACK_HANDLER_DATA_COMPONENT =
            DATA_COMPONENTS.registerComponentType(
                    "item_stack_handler_data",
                    builder -> builder.persistent(ItemStackHandlerDataComponent.CODEC)
                            .networkSynchronized(ItemStackHandlerDataComponent.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SelectedSlotDataComponent>>
            SELECTED_SLOT_DATA_COMPONENT =
            DATA_COMPONENTS.registerComponentType(
                    "selected_slot",
                    builder -> builder.persistent(SelectedSlotDataComponent.CODEC)
                            .networkSynchronized(SelectedSlotDataComponent.STREAM_CODEC)
            );
}
