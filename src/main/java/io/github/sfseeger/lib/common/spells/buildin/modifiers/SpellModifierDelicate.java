package io.github.sfseeger.lib.common.spells.buildin.modifiers;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SpellModifierDelicate extends AbstractSpellModifier {
    public static final SpellModifierDelicate INSTANCE = new SpellModifierDelicate();

    public SpellModifierDelicate() {
        super(Map.of(Manas.OrderMana, 3), 1);
    }

    @Override
    public void onGatherContext(@Nullable HitResult rayTrace, SpellCastingContext context) {
        context.setVariable("delicate", true);
    }
}
