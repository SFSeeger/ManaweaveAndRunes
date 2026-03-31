package io.github.sfseeger.lib.common.rituals.state_machine;

public enum RitualStepResult {
    SUCCESS,
    SKIP,
    END,
    FAIL;

    public RitualStepResult getHigherPriority(RitualStepResult other) {
        return this.ordinal() > other.ordinal() ? this : other;
    }

    public boolean isEnding() {
        return this == END || this == FAIL;
    }
}
