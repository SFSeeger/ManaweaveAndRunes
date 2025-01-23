package io.github.sfseeger.lib.common.spells;

public enum SpellCastingResult {
    SKIPPED,
    SUCCESS,
    FAILURE;


    public SpellCastingResult compare(SpellCastingResult other){
        return this.ordinal() >= other.ordinal() ? this: other;
    }
}
