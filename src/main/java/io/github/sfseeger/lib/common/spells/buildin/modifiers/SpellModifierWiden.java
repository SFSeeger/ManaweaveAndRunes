package io.github.sfseeger.lib.common.spells.buildin.modifiers;

import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class SpellModifierWiden extends AbstractSpellModifier {
    public static final AbstractSpellModifier INSTANCE = new SpellModifierWiden();

    public SpellModifierWiden() {
        super();
    }

    @Override
    public void onGatherContext(@Nullable HitResult rayTrace, SpellCastingContext context) {
        Object width = context.getVariable("width");
        if (width == null) width = 1;
        //if((int)width == 7) return;
        context.setVariable("width", (int) width + 2);
    }
}
