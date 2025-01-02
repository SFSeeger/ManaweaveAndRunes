package io.github.sfseeger.lib.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Rituals {
    public static final Ritual DEFAULT_RITUAL = new Ritual(Tier.NOVICE, 0) {
        @Override
        public Vec3 getDimension() {
            return Vec3.ZERO;
        }

        @Override
        public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
                RitualContext context, RitualOriginType originType) {
            return RitualStepResult.END;
        }

        @Override
        public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
                RitualOriginType originType) {
        }

        @Override
        public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
                RitualOriginType originType) {
        }
    };
}
