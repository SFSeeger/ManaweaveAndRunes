package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.phys.HitResult;

import java.util.Arrays;

public class SpellResolver {
    private final Spell spell;

    public SpellResolver(Spell spell) {
        this.spell = spell;
    }

    public SpellCastingResult resolve(HitResult rayTrace, SpellCastingContext context) {
        for(AbstractSpellEffect effect : spell.getEffects()){
            SpellCastingContext localContext = context.clone();
            AbstractSpellModifier[] modifiers = spell.getModifiers().get(effect);
            Arrays.stream(modifiers).forEach(modifier -> {
                modifier.onGatherContext(rayTrace, localContext);
                modifier.preResolve(rayTrace, localContext);
            });
            effect.resolve(rayTrace, localContext);
            Arrays.stream(modifiers).forEach(modifier -> modifier.postResolve(rayTrace, localContext));
        }
        return SpellCastingResult.SUCCESS;
    }
}
