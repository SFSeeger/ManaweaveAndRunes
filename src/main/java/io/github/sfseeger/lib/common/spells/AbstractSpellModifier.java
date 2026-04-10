package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public abstract class AbstractSpellModifier extends AbstractSpellNode {
    public AbstractSpellModifier(Map<Mana, Integer> baseCosts, int baseCooldown) {
        super(baseCosts, baseCooldown);
    }

    public void onGatherContext(@Nullable HitResult rayTrace, SpellCastingContext context) {
    }

    public void preResolve(@Nullable HitResult rayTrace, SpellCastingContext context) {
    }

    public void postResolve(@Nullable HitResult rayTrace, SpellCastingContext context) {
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of();
    }

    @Override
    public @NotNull SpellNodeType getSpellNodeType() {
        return SpellNodeType.MODIFIER;
    }
}
