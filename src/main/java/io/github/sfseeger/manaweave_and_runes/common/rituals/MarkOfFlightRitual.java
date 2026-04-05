package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.manaweave_and_runes.core.init.MarkInit;
import net.minecraft.world.phys.Vec3;

public class MarkOfFlightRitual extends MarkRitual {
    public MarkOfFlightRitual() {
        super(Tier.ASCENDED, 1);
    }

    @Override
    public void onRitualAbort(RitualStateMachineContext ctx) {
        RitualUtils.getStartingPlayer(ctx).ifPresent(player -> {
            player.addDeltaMovement(new Vec3(0, 30, 0));
        });
    }

    @Override
    public MarkInstance createMark(RitualStateMachineContext ctx) {
        return new MarkInstance(MarkInit.MARK_OF_FLIGHT.get(), 1);
    }
}
