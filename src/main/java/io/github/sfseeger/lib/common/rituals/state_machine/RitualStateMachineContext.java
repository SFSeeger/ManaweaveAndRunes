package io.github.sfseeger.lib.common.rituals.state_machine;

import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.rituals.Ritual;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record RitualStateMachineContext(Level level, BlockPos pos, BlockState state, Ritual.RitualOriginType originType,
                                        int ticksPassed, Ritual ritual, ContextMap contextMap) {
}
