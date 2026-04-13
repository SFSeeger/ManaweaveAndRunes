package io.github.sfseeger.lib.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
import net.minecraft.core.BlockPos;
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
        public RitualStepResult onRitualServerTick(RitualStateMachineContext ctx) {
            return RitualStepResult.END;
        }

        @Override
        public void onRitualEnd(RitualStateMachineContext ctx) {
        }

        @Override
        public void onRitualAbort(Level level, BlockPos pos, BlockState state, ContextMap context,
                                  RitualOriginType originType) {
        }
    };
}
