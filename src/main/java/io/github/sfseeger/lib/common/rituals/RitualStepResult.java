package io.github.sfseeger.lib.common.rituals;

public enum RitualStepResult {
    SUCCESS,
    SKIP,
    END,
    ABORT;

    public RitualStepResult getHigherPriority(RitualStepResult other) {
        return this.ordinal() > other.ordinal() ? this : other;
    }

    public boolean isEnding() {
        return this == END || this == ABORT;
    }
}
