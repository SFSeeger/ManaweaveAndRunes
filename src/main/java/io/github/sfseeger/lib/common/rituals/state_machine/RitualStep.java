package io.github.sfseeger.lib.common.rituals.state_machine;

import java.util.function.Function;

public class RitualStep {
    String name;
    int stepNumber; // Used to identify step when serializing / deserializing nbt;
    Function<RitualStateMachineContext, RitualStepResult> action;

    public RitualStep(String name, int stepNumber, Function<RitualStateMachineContext, RitualStepResult> action) {
        this.name = name;
        this.stepNumber = stepNumber;
        this.action = action;
    }

    RitualStepResult execute(RitualStateMachineContext ctx) {
        return action.apply(ctx);
    }
}
