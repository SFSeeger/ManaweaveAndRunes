package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.world.phys.HitResult;

import java.util.Map;
import java.util.Set;

public abstract class AbstractSpellModifier extends AbstractSpellNode {
    public AbstractSpellModifier(Map<Mana, Integer> baseCosts, int baseCooldown) {
        super(baseCosts, baseCooldown);
    }

    public void onGatherContext(HitResult rayTrace, SpellCastingContext context) {
    }

    public void preResolve(HitResult rayTrace, SpellCastingContext context) {
    }

    public void postResolve(HitResult rayTrace, SpellCastingContext context) {
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of();
    }
}
