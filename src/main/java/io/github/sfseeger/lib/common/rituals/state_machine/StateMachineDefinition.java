package io.github.sfseeger.lib.common.rituals.state_machine;

import java.util.List;

public record StateMachineDefinition(
        List<RitualStep> initialSteps,
        List<RitualStep> tickSteps,
        RitualStep abortStep,
        RitualStep finishStep,
        Runnable onStateChange
) {
}
