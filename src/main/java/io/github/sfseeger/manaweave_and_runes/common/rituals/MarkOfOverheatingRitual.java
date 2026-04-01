package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.lib.common.rituals.marks.MarkDataAttachment;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataAttachmentInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MarkInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.PLAYER_TYPE;

public class MarkOfOverheatingRitual extends CurseRitual {
    public MarkOfOverheatingRitual() {
        super(Tier.NOVICE, 1);
    }

    @Override
    public Vec3 getDimension() {
        return null;
    }


    @Override
    public void onRitualAbort(RitualStateMachineContext ctx) {
        RitualUtils.getStartingPlayer(ctx).ifPresent(player -> player.igniteForTicks(30));
    }

    @Override
    public MarkInstance createMark(RitualStateMachineContext ctx) {
        return new MarkInstance(MarkInit.MARK_OF_OVERHEATING.get());
    }
}
