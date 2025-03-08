package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.Rituals;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.rituals.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RitualInit {
    public static final DeferredRegister<Ritual> RITUALS =
            DeferredRegister.create(ManaweaveAndRunesRegistries.RITUAL_REGISTRY, ManaweaveAndRunes.MODID);

    public static final Supplier<Ritual> DEFAULT_RITUAL =
            RITUALS.register("default_ritual", () -> Rituals.DEFAULT_RITUAL);
    public static final Supplier<Ritual> PARTICLE_RITUAL =
            RITUALS.register("particle_ritual", ParticleRitual::new);
    public static final Supplier<Ritual> FLIGHT_RITUAL =
            RITUALS.register("flight_ritual", FlightRitual::new);
    public static final Supplier<Ritual> THUNDER_RITUAL =
            RITUALS.register("thunder_ritual", ThunderRitual::new);
    public static final Supplier<Ritual> TELEPORT_RITUAL =
            RITUALS.register("teleport_ritual", TeleportRitual::new);
    public static final Supplier<Ritual> GROWTH_RITUAL =
            RITUALS.register("growth_ritual", GrowthRitual::new);
    public static final Supplier<Ritual> SANCTUARY_RITUAL =
            RITUALS.register("sanctuary_ritual", () -> new SanctuaryRitual());
    public static final Supplier<Ritual> ASCENDED_SANCTUARY_RITUAL = RITUALS.register("ascended_sanctuary_ritual",
                                                                                      AscendedSanctuaryRitual::new);
    public static final Supplier<ShatteringRiteRitual> SHATTERING_RITE_RITUAL =
            RITUALS.register("shattering_rite_ritual", ShatteringRiteRitual::new);

    public static final Supplier<SmiteRitual> SMITE_RITUAL = RITUALS.register("smite_ritual", SmiteRitual::new);
}
