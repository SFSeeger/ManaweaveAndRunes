package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.rituals.marks.Mark;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.marks.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MarkInit {
    public static final DeferredRegister<Mark> MARKS =
            DeferredRegister.create(ManaweaveAndRunesRegistries.MARK_REGISTRY, ManaweaveAndRunes.MODID);
    public static final Supplier<Mark> MARK_OF_OVERHEATING =
            MARKS.register("mark_of_overheating", MarkOfOverheating::new);
    public static final Supplier<Mark> MARK_OF_SINKING = MARKS.register("mark_of_sinking", MarkOfSinking::new);

    public static final Supplier<Mark> MARK_OF_GROWTH = MARKS.register("mark_of_growth", MarkOfGrowth::new);
    public static final Supplier<Mark> MARK_OF_SHRINKING = MARKS.register("mark_of_shrinking", MarkOfShrinking::new);
    public static final Supplier<Mark> MARK_OF_FLIGHT = MARKS.register("mark_of_flight", MarkOfFlight::new);
}
