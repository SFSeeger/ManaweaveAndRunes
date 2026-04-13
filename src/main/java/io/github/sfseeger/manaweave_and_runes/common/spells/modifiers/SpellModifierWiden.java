package io.github.sfseeger.manaweave_and_runes.common.spells.modifiers;

import io.github.sfseeger.lib.common.context_data_types.builtin.FloatContextDataType;
import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class SpellModifierWiden extends AbstractSpellModifier {
    public static final AbstractSpellModifier INSTANCE = new SpellModifierWiden();

    public SpellModifierWiden() {
        super();
    }

    @Override
    public void onGatherContext(@Nullable HitResult rayTrace, AbstractSpellCastingContext context) {
        float width = context.getFloatContextData("width", 1f);
        context.getContextData().putData("width", new FloatContextDataType(width + 1));
    }
}
