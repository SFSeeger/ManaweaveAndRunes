package io.github.sfseeger.lib.common.spells.buildin.modifiers;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public class SpellModifierStrengthen extends AbstractSpellModifier {
    public static final SpellModifierStrengthen INSTANCE = new SpellModifierStrengthen();

    public SpellModifierStrengthen() {
        super(Map.of(Manas.OrderMana, 6), 8);
    }

    @Override
    public void onGatherContext(HitResult rayTrace, SpellCastingContext context) {
        float strength = (float)context.getVariable("strength");
        context.setVariable("strength", strength + 1);
    }
}
