package io.github.sfseeger.lib.common.spells.buildin.modifiers;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public class SpellModifierElongate extends AbstractSpellModifier {
    public static final AbstractSpellNode INSTANCE = new SpellModifierElongate();

    public SpellModifierElongate() {
        super(Map.of(Manas.AirMana, 5), 1);
    }

    @Override
    public void onGatherContext(HitResult rayTrace, SpellCastingContext context) {
        Object height = context.getVariable("height");
        if (height == null) height = 1;
        //if((int)height == 7) return;
        context.setVariable("height", (int) height + 2);
    }
}
