package io.github.sfseeger.manaweave_and_runes.common.spells.modifiers;

import io.github.sfseeger.lib.common.context_data_types.builtin.FloatContextDataType;
import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class SpellModifierStrengthen extends AbstractSpellModifier {
    public static final SpellModifierStrengthen INSTANCE = new SpellModifierStrengthen();

    public SpellModifierStrengthen() {
        super();
    }

    @Override
    public void onGatherContext(@Nullable HitResult rayTrace, AbstractSpellCastingContext context) {
        float strength = context.getFloatContextData("strength", 1f);
        context.getContextData().putData("strength", new FloatContextDataType(strength + 1));
    }
}
