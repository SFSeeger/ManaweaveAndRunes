package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorBlock;
import io.github.sfseeger.manaweave_and_runes.core.util.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ParticleRitual extends Ritual {
    public ParticleRitual() {
        super(Tier.NOVICE, 5 * 20);
    }

    @Override
    public Vec3 getDimension() {
        return new Vec3(10, 10, 10);
    }

    @Override
    public RitualStepResult onRitualServerTick(RitualStateMachineContext ctx) {
        if (ctx.ticksPassed() % 10 == 0) {
            if (ctx.level().isThundering()) return RitualStepResult.FAIL;
        }
        int amount = 1;
        if (ctx.level().getBlockState(ctx.pos()).getBlock() instanceof RitualAnchorBlock block) {
            amount = block.ritualAnchorType.getTier().ordinal() + 1;
        }

        Vec3 d = getDimension();
        for (int i = 0; i < 5; i++) {
            Vec3 randomPos =
                    ParticleUtils.randomPosInsideBox(ctx.pos(), ctx.level().getRandom(), -d.x / 2, -d.y / 2, -d.z / 2,
                                                     d.x / 2,
                                                     d.y / 2,
                                                     d.z / 2);
            ((ServerLevel) ctx.level()).sendParticles(ParticleTypes.GLOW, randomPos.x(), randomPos.y(), randomPos.z(),
                                                      amount, 0, 0, 0,
                                                      0);
        }
        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualClientTick(Level level, BlockPos pos, BlockState state, int ticksPassed,
                                   ContextMap context, RitualOriginType originType
    ) {
        if (ticksPassed % 10 == 0) {
            level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1, 1);
        }
    }

    @Override
    public void onRitualEnd(RitualStateMachineContext ctx) {
        ctx.level().playSound(null, ctx.pos(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1, 1);
    }

    @Override
    public void onRitualAbort(Level level, BlockPos pos, BlockState state, ContextMap context,
                              RitualOriginType originType
    ) {
    }

    @Override
    public boolean usableInSpellcastingCircle() {
        return true;
    }
}
