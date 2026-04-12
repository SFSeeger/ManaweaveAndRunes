package io.github.sfseeger.lib.common.spells.buildin.modifiers;

import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class SpellModifierDelicate extends AbstractSpellModifier {
    public static final SpellModifierDelicate INSTANCE = new SpellModifierDelicate();

    public SpellModifierDelicate() {
        super();
    }

    @Override
    public void onGatherContext(@Nullable HitResult rayTrace, SpellCastingContext context) {
        context.setVariable("delicate", true);
    }
}
