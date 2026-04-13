package io.github.sfseeger.manaweave_and_runes.common.spells.modifiers;

import io.github.sfseeger.lib.common.context_data_types.ContextDataTypes;
import io.github.sfseeger.lib.common.context_data_types.builtin.FloatContextDataType;
import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class SpellModifierElongate extends AbstractSpellModifier {
    public static final AbstractSpellNode INSTANCE = new SpellModifierElongate();

    public SpellModifierElongate() {
        super();
    }

    @Override
    public void onGatherContext(@Nullable HitResult rayTrace, AbstractSpellCastingContext context) {
        float height = context.getFloatContextData("height", 1f);
        context.getContextData().putData("height", new FloatContextDataType(height + 2));
    }
}
