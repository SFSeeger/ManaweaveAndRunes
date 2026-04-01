package io.github.sfseeger.lib.common.rituals.state_machine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RitualStateMachine {
    private final StateMachineDefinition machineDefinition;
    private RitualStepId stepId = RitualStepId.IDLE;

    public RitualStateMachine(StateMachineDefinition definition) {
        this.machineDefinition = definition;
    }

    public boolean start() {
        if (stepId == RitualStepId.IDLE) {
            stepId = RitualStepId.STARTING;
            return true;
        }
        return false;
    }

    public void tick(RitualStateMachineContext ctx) {
        switch (stepId) {
            case IDLE -> {
                // do nothing
            }
            case STARTING -> {
                setStepId(RitualStepId.PRE_TICK_LOOP);
            }
            case PRE_TICK_LOOP -> {
                for (RitualStep step : machineDefinition.initialSteps()) {
                    RitualStepResult result = step.execute(ctx);
                    switch (result) {
                        case SKIP -> setStepId(RitualStepId.TICK_LOOP);
                        case END -> setStepId(RitualStepId.FINISH);
                        case FAIL -> setStepId(RitualStepId.ABORT);
                    }
                }
                break;
            }
            case TICK_LOOP -> {
                for (RitualStep step : machineDefinition.tickSteps()) {
                    RitualStepResult result = step.execute(ctx);
                    switch (result) {
                        case END -> setStepId(RitualStepId.FINISH);
                        case FAIL -> setStepId(RitualStepId.ABORT);
                        case SUCCESS, SKIP -> {

                        }
                    }
                }
                break;
            }
            case ABORT -> {
                machineDefinition.abortStep().execute(ctx);
                setStepId(RitualStepId.IDLE);
            }
            case FINISH -> {
                machineDefinition.finishStep().execute(ctx);
                setStepId(RitualStepId.IDLE);
            }
        }
    }

    public void sendAbort() {
        if (stepId != RitualStepId.IDLE) {
            setStepId(RitualStepId.ABORT);
        }
    }
    public void sendFinish() {
        if (stepId != RitualStepId.IDLE) {
            setStepId(RitualStepId.FINISH);
        }
    }

    public RitualStepId getStepId() {
        return stepId;
    }

    /*
    Dangerously set the step id. Should only be used for deserialization, and should be used with caution as it can lead to invalid states if set incorrectly.
     */
    public void setStepId(RitualStepId stepId) {
        this.stepId = stepId;
        this.machineDefinition.onStateChange().run();
    }

    public static class Builder {
        List<RitualStep> tickSteps = new ArrayList<>();
        List<RitualStep> preTickSteps = new ArrayList<>();
        RitualStep abortStep = null;
        RitualStep finishStep = null;
        Runnable onStateChange = () -> {};
        int stepCounter = 0;

        public Builder withTickStep(String name, Function<RitualStateMachineContext, RitualStepResult> action) {
            tickSteps.add(new RitualStep(name, stepCounter++, action));
            return this;
        }

        public Builder withPreTickStep(String name, Function<RitualStateMachineContext, RitualStepResult> action) {
            preTickSteps.add(new RitualStep(name, stepCounter++, action));
            return this;
        }

        public Builder withAbortStep(String name, Function<RitualStateMachineContext, RitualStepResult> action) {
            abortStep = new RitualStep(name, stepCounter++, action);
            return this;
        }

        public Builder withFinishStep(String name, Function<RitualStateMachineContext, RitualStepResult> action) {
            finishStep = new RitualStep(name, stepCounter++, action);
            return this;
        }

        public Builder withOnStateChange(Runnable onStateChange) {
            this.onStateChange = onStateChange;
            return this;
        }

        public RitualStateMachine build() {
            if (abortStep == null) {
                abortStep = new RitualStep("Default Abort Step", stepCounter++, ctx -> RitualStepResult.SUCCESS);
            }
            if (finishStep == null) {
                finishStep = new RitualStep("Default Finish Step", stepCounter++, ctx -> RitualStepResult.SUCCESS);
            }
            return new RitualStateMachine(new StateMachineDefinition(preTickSteps, tickSteps, abortStep, finishStep, onStateChange));
        }
    }
}
