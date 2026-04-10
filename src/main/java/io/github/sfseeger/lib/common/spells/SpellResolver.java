package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

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
        SpellCastingContext localContext =
                new SpellCastingContext(context.getLevel(), context.getCaster(), context.getHandIn());
        SpellCastingResult result = spell.resolveEffects(rayTrace, localContext);
        spell.getCore().getModifiers().forEach(modifier -> modifier.postResolve(rayTrace, context));
        return result;
    }
}
