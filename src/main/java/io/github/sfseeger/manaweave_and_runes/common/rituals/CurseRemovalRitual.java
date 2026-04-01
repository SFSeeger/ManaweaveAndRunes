package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.rituals.marks.MarkDataAttachment;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.lib.common.rituals.marks.MarkType;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.BooleanRitualData;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataAttachmentInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CurseRemovalRitual extends Ritual {
    public CurseRemovalRitual() {
        super(Tier.NOVICE, 1);
    }

    @Override
    public Vec3 getDimension() {
        return null;
    }

    @Override
    public RitualStepResult onRitualServerTick(RitualStateMachineContext ctx) {
        ctx.ritualContext().putData("curse_removed", new BooleanRitualData(false));

        ServerLevel level = (ServerLevel) ctx.level();
        UUID playerUUID = ctx.ritualContext().getData(RitualDataTypes.PLAYER_TYPE).getPlayerUUID();
        Player player = level.getPlayerByUUID(playerUUID);
        if (player == null) return RitualStepResult.FAIL;


        int curseLevel;
        if (level.getBlockState(ctx.pos()).getBlock() instanceof RitualAnchorBlock block) {
            curseLevel = block.ritualAnchorType.getTier().ordinal() + 1;
        } else {
            curseLevel = 1;
        }

        MarkDataAttachment marks = player.getData(MRDataAttachmentInit.MARKS_DATA_ATTACHMENT_TYPE);
        List<MarkInstance> playerMarks = marks.getMarks();
        List<MarkInstance> playerCurses = playerMarks
                .stream()
                .filter(mark -> mark.getStrength() <= curseLevel && mark.getMarkType() == MarkType.CURSE)
                .toList();

        if (playerCurses.isEmpty()) {
            if (playerMarks.stream().anyMatch(mark -> mark.getMarkType() == MarkType.CURSE)) {
                // Ritual Altar was not strong enough to remove any present curse
                return RitualStepResult.FAIL;
            }
            return RitualStepResult.SUCCESS;
        }
        playerMarks.remove(playerCurses.get(level.getRandom().nextInt(playerCurses.size())));
        player.setData(MRDataAttachmentInit.MARKS_DATA_ATTACHMENT_TYPE, marks);
        ctx.ritualContext().putData("curse_removed", new BooleanRitualData(true));

        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(RitualStateMachineContext ctx) {
        BlockPos pos = ctx.pos();
        BooleanRitualData ritualData = ctx.ritualContext().getData("curse_removed", RitualDataTypes.BOOLEAN_TYPE);
        if (ritualData != null && ritualData.value()) {
            ((ServerLevel) ctx.level()).sendParticles(ParticleTypes.GLOW, pos.getX() + 0.5, pos.getY() + 2,
                                                      pos.getZ() + 0.5, 20, 0.5, 0.5, 0.5, 0.1);
        } else {
            ((ServerLevel) ctx.level()).sendParticles(ParticleTypes.ASH, pos.getX() + 0.5, pos.getY() + 2,
                                                      pos.getZ() + 0.5, 20, 0.5, 0.5, 0.5, 0.1);

        }
    }

    @Override
    public void onRitualAbort(RitualStateMachineContext ctx) {
        Optional<Player> player = RitualUtils.getStartingPlayer(ctx);
        if (player.isPresent() && player.get().isAlive()) {
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) ctx.level(), player.get().getOnPos(), MobSpawnType.TRIGGERED);
        }
    }
}
