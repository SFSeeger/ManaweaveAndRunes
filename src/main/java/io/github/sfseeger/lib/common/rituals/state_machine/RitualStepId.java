package io.github.sfseeger.lib.common.rituals.state_machine;

public enum RitualStepId {
    IDLE,
    STARTING,
    PRE_TICK_LOOP,
    TICK_LOOP,
    FINISH,
    ABORT,
}
