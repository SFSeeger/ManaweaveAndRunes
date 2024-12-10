package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.mana.ManaDateComponent;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ManaweaveAndRunesDataComponentsInit {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ManaweaveAndRunes.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ManaDateComponent>> MANA_DATA_COMPONENT = DATA_COMPONENTS.registerComponentType(
            "mana_data",
            builder -> builder.persistent(ManaDateComponent.CODEC).networkSynchronized(ManaDateComponent.STREAM_CODEC)
    );
}
