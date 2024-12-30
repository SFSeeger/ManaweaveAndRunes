package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.Rituals;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RitualInit {
    public static final DeferredRegister<Ritual> RITUALS =
            DeferredRegister.create(ManaweaveAndRunesRegistries.RITUAL_REGISTRY, ManaweaveAndRunes.MODID);

    public static final Supplier<Ritual> DEFAULT_RITUAL =
            RITUALS.register("default_ritual", () -> Rituals.DEFAULT_RITUAL);
}
