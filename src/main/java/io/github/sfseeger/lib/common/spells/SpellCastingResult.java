package io.github.sfseeger.lib.common.spells;

public record SpellCastingResult(boolean success) {
    public static final SpellCastingResult FAILED = new SpellCastingResult(false);
    public static final SpellCastingResult SUCCESS = new SpellCastingResult(true);
    ;
}
