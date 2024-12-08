package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.api.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.common.api.mana.ManaProperties;
import io.github.sfseeger.manaweave_and_runes.common.api.mana.ManaRegistry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ManaweaveAndRunesManaInit {
    public static final DeferredRegister<Mana> MANA_TYPES = DeferredRegister.create(ManaRegistry.MANA_REGISTRY, ManaweaveAndRunes.MODID);

    public static final Supplier<Mana> EMPTY_MANA = MANA_TYPES.register("empty_mana", () -> new Mana(
            new ManaProperties.Builder().build()));
    public static final Supplier<Mana> FIRE_MANA = MANA_TYPES.register("fire_mana", () -> new Mana(
            new ManaProperties.Builder().color(0xFF0000).build()));
}
