package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.marks.MarkDataAttachment;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
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

public abstract class CurseRitual extends Ritual {
    public CurseRitual(Tier tier, int duration) {
        super(tier, duration);
    }

    @Override
    public Vec3 getDimension() {
        return null;
    }

    @Override
    public RitualStepResult onRitualServerTick(RitualStateMachineContext ctx) {
        RandomSource random = ctx.level().random;
        if (random.nextInt(100) > 90) {
            return RitualStepResult.FAIL;
        }
        PlayerRitualData playerRitualData = ctx.ritualContext().getData(PLAYER_TYPE);
        if (playerRitualData == null) {
            return RitualStepResult.FAIL;
        }
        Player target = ctx.level().getServer().getPlayerList().getPlayer(playerRitualData.getPlayerUUID());
        if (target == null) {
            return RitualStepResult.FAIL;
        }
        MarkDataAttachment marks = target.getData(MRDataAttachmentInit.MARKS_DATA_ATTACHMENT_TYPE);
        marks.addMark(createMark(ctx));
        target.setData(MRDataAttachmentInit.MARKS_DATA_ATTACHMENT_TYPE, marks);
        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(RitualStateMachineContext ctx) {
        BlockPos pos = ctx.pos();
        ((ServerLevel) ctx.level()).sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 2,
                                                  pos.getZ() + 0.5, 20, 0.5, 0.5, 0.5, 0.1);
    }

    @Override
    public abstract void onRitualAbort(RitualStateMachineContext ctx);

    public abstract MarkInstance createMark(RitualStateMachineContext ctx);
}
