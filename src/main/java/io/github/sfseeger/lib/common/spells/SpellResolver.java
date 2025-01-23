package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import java.util.Arrays;
import java.util.Map;

public class SpellResolver {
    private final Spell spell;

    public SpellResolver(Spell spell) {
        this.spell = spell;
    }

    public SpellCastingResult resolve(UseOnContext useOnContext, SpellCastingContext context){
        return resolve(new BlockHitResult(
                useOnContext.getClickLocation(),
                useOnContext.getClickedFace(),
                useOnContext.getClickedPos(), false),
                context);
    }

    public SpellCastingResult resolve(HitResult rayTrace, SpellCastingContext context) {
        SpellCastingResult result = SpellCastingResult.SUCCESS;
        for(AbstractSpellEffect effect : spell.getEffects()){
            SpellCastingContext localContext = context.clone();
            AbstractSpellModifier[] modifiers = spell.getModifiers().get(effect);
            Arrays.stream(modifiers).forEach(modifier -> {
                modifier.onGatherContext(rayTrace, localContext);
                modifier.preResolve(rayTrace, localContext);
            });
            result = effect.resolve(rayTrace, localContext).compare(result);
            Arrays.stream(modifiers).forEach(modifier -> modifier.postResolve(rayTrace, localContext));
        }
        return result;
    }
}
