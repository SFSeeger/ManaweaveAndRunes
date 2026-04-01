package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
import io.github.sfseeger.manaweave_and_runes.core.init.MarkInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

public class MarkOfSinkingRitual extends CurseRitual {

    public MarkOfSinkingRitual() {
        super(Tier.MASTER, 1);
    }

    @Override
    public void onRitualAbort(RitualStateMachineContext ctx) {
        RitualUtils.getStartingPlayer(ctx).ifPresent(player -> {
            player.setDeltaMovement(player.getDeltaMovement().add(0, 10.0, 0));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 30));
        });
    }

    @Override
    public MarkInstance createMark(RitualStateMachineContext ctx) {
        return new MarkInstance(MarkInit.MARK_OF_SINKING.get(), 2);
    }
}
