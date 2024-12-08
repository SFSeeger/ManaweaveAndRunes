package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.mana.Mana;
import io.github.sfseeger.lib.mana.ManaProperties;
import io.github.sfseeger.lib.mana.ManaRegistry;
import io.github.sfseeger.lib.mana.utils.ManaGenerationHelper;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ManaInit {
    public static final DeferredRegister<Mana> MANA_TYPES =
            DeferredRegister.create(ManaRegistry.MANA_REGISTRY, ManaweaveAndRunes.MODID);

    public static final Supplier<Mana> EMPTY_MANA = MANA_TYPES.register("empty_mana", () -> new Mana(
            new ManaProperties.Builder().build()));
    public static final Supplier<Mana> FIRE_MANA = MANA_TYPES.register("fire_mana", () -> new Mana(
            new ManaProperties.Builder().color(0xFF0000).addGenerationCondition(
                    ManaGenerationHelper.GenerationCondition.SURROUNDED_BY_LAVA).canBeGenerated(true).build()));
    public static final Supplier<Mana> AIR_MANA = MANA_TYPES.register("air_mana", () -> new Mana(
            new ManaProperties.Builder().color(0xADF3FF).addGenerationCondition(
                    ManaGenerationHelper.GenerationCondition.PLACED_HIGH).canBeGenerated(true).build()));
}
