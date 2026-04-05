package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.manaweave_and_runes.core.init.MarkInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class MarkOfGrowthRitual extends MarkRitual {
    public MarkOfGrowthRitual() {
        super(Tier.MASTER, 1);
    }

    @Override
    public void onRitualAbort(RitualStateMachineContext ctx) {
        RitualUtils.getStartingPlayer(ctx).ifPresent(player -> {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
        });
    }

    @Override
    public MarkInstance createMark(RitualStateMachineContext ctx) {
        return new MarkInstance(MarkInit.MARK_OF_GROWTH.get(), 1);
    }
}
