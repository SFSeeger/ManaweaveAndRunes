package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class AbstractSpellModifier extends AbstractSpellNode {
    public AbstractSpellModifier() {
        super();
    }

    public void onGatherContext(@Nullable HitResult rayTrace, AbstractSpellCastingContext context) {
    }

    public void preResolve(@Nullable HitResult rayTrace, AbstractSpellCastingContext context) {
    }

    public void postResolve(@Nullable HitResult rayTrace, AbstractSpellCastingContext context) {
    }

    @Override
    public @NotNull SpellNodeType getSpellNodeType() {
        return SpellNodeType.MODIFIER;
    }
}
