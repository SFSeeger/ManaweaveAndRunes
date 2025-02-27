package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ManaInit {
    public static final DeferredRegister<Mana> MANA_TYPES =
            DeferredRegister.create(ManaweaveAndRunesRegistries.MANA_REGISTRY, ManaweaveAndRunes.MODID);

    public static final Supplier<Mana> EMPTY_MANA = MANA_TYPES.register("empty_mana", () -> Manas.EmptyMana);
    public static final Supplier<Mana> FIRE_MANA = MANA_TYPES.register("fire_mana", () -> Manas.FireMana);
    public static final Supplier<Mana> AIR_MANA = MANA_TYPES.register("air_mana", () -> Manas.AirMana);
    public static final Supplier<Mana> WATER_MANA = MANA_TYPES.register("water_mana", () -> Manas.WaterMana);
    public static final Supplier<Mana> EARTH_MANA = MANA_TYPES.register("earth_mana", () -> Manas.EarthMana);
    public static final Supplier<Mana> VOID_MANA = MANA_TYPES.register("void_mana", () -> Manas.VoidMana);
    public static final Supplier<Mana> SOUL_MANA = MANA_TYPES.register("soul_mana", () -> Manas.SoulMana);
    public static final Supplier<Mana> ENTROPY_MANA = MANA_TYPES.register("entropy_mana", () -> Manas.EntropyMana);
    public static final Supplier<Mana> ORDER_MANA = MANA_TYPES.register("order_mana", () -> Manas.OrderMana);
}
