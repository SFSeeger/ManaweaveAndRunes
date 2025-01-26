package io.github.sfseeger.lib.common.spells;

public enum SpellCastingResult {
    SKIPPED(true),
    SUCCESS(true),
    FAILURE(false);

    private final boolean success;

    SpellCastingResult(boolean success) {
        this.success = success;
    }
    public SpellCastingResult compare(SpellCastingResult other){
        return this.ordinal() >= other.ordinal() ? this: other;
    }

    public boolean isSuccess() {
        return success;
    }
}
