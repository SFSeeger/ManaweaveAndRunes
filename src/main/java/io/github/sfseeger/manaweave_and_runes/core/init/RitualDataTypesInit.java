package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataType;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RitualDataTypesInit {
    public static final DeferredRegister<RitualDataType<?>> RITUAL_DATA_TYPES =
            DeferredRegister.create(ManaweaveAndRunesRegistries.RITUAL_DATA_TYPE_REGISTRY, ManaweaveAndRunes.MODID);

    public static void register(IEventBus eventBus) {
        RitualDataTypes.register(RITUAL_DATA_TYPES);
        RITUAL_DATA_TYPES.register(eventBus);
    }
}
