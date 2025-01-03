package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
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
    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {
        if (ticksPassed % 10 == 0) {
            if (level.isThundering()) return RitualStepResult.ABORT;
        }
        int amount = 1;
        if (level.getBlockState(pos).getBlock() instanceof RitualAnchorBlock block) {
            amount = block.ritualAnchorType.getTier().ordinal() + 1;
        }

        Vec3 d = getDimension();
        for (int i = 0; i < 5; i++) {
            Vec3 randomPos =
                    ParticleUtils.randomPosInsideBox(pos, level.getRandom(), -d.x / 2, -d.y / 2, -d.z / 2, d.x / 2,
                                                     d.y / 2,
                                                     d.z / 2);
            level.sendParticles(ParticleTypes.GLOW, randomPos.x(), randomPos.y(), randomPos.z(), amount, 0, 0, 0,
                                0);
        }
        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualClientTick(Level level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {
        if (ticksPassed % 10 == 0) {
            level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1, 1);
        }
    }

    @Override
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        level.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1, 1);
    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 3, Level.ExplosionInteraction.TNT);
    }

    @Override
    public boolean usableInSpellcastingCircle() {
        return true;
    }
}
