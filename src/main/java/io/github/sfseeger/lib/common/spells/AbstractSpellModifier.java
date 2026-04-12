package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class AbstractSpellModifier extends AbstractSpellNode {
    public AbstractSpellModifier() {
        super();
    }

    public void onGatherContext(@Nullable HitResult rayTrace, SpellCastingContext context) {
    }

    public void preResolve(@Nullable HitResult rayTrace, SpellCastingContext context) {
    }

    public void postResolve(@Nullable HitResult rayTrace, SpellCastingContext context) {
    }

    @Override
    public @NotNull SpellNodeType getSpellNodeType() {
        return SpellNodeType.MODIFIER;
    }
}
