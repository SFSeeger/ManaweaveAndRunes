package io.github.sfseeger.lib.common.rituals.state_machine;

import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record RitualStateMachineContext(Level level, BlockPos pos, BlockState state, Ritual.RitualOriginType originType,
                                        int ticksPassed, Ritual ritual, RitualContext ritualContext) {
}
