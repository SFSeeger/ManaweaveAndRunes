package io.github.sfseeger.manaweave_and_runes.common.spells.modifiers;

import io.github.sfseeger.lib.common.context_data_types.builtin.BooleanContextDataType;
import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class SpellModifierDelicate extends AbstractSpellModifier {
    public static final SpellModifierDelicate INSTANCE = new SpellModifierDelicate();

    public SpellModifierDelicate() {
        super();
    }

    @Override
    public void onGatherContext(@Nullable HitResult rayTrace, AbstractSpellCastingContext context) {
        context.getContextData().putData("delicate", new BooleanContextDataType(true));
    }
}
