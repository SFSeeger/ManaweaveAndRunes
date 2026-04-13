package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.context_data_types.ContextDataType;
import io.github.sfseeger.lib.common.context_data_types.ContextDataTypes;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RitualDataTypesInit {
    public static final DeferredRegister<ContextDataType<?>> RITUAL_DATA_TYPES =
            DeferredRegister.create(ManaweaveAndRunesRegistries.CONTEXT_DATA_TYPE_REGISTRY, ManaweaveAndRunes.MODID);

    public static void register(IEventBus eventBus) {
        ContextDataTypes.register(RITUAL_DATA_TYPES);
        RITUAL_DATA_TYPES.register(eventBus);
    }
}
