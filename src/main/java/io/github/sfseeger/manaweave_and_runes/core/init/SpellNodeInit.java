package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.lib.common.spells.buildin.effects.SpellEffectBreak;
import io.github.sfseeger.lib.common.spells.buildin.effects.SpellEffectBurn;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import io.github.sfseeger.lib.common.spells.buildin.types.SpellTypeProjectile;
import io.github.sfseeger.lib.common.spells.buildin.types.SpellTypeSelf;
import io.github.sfseeger.lib.common.spells.buildin.types.SpellTypeTouch;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SpellNodeInit {
    public static final DeferredRegister<AbstractSpellNode> SPELL_NODES =
            DeferredRegister.create(ManaweaveAndRunesRegistries.SPELL_NODE_REGISTRY, ManaweaveAndRunes.MODID);

    public static final Supplier<AbstractSpellNode> SPELL_TYPE_SELF =
            SPELL_NODES.register("spell_type.self", () -> SpellTypeSelf.INSTANCE);
    public static final Supplier<AbstractSpellNode> SPELL_TYPE_TOUCH =
            SPELL_NODES.register("spell_type.touch", () -> SpellTypeTouch.INSTANCE);
    public static final Supplier<AbstractSpellNode> SPELL_TYPE_PROJECTILE =
            SPELL_NODES.register("spell_type.projectile", () -> SpellTypeProjectile.INSTANCE);

    public static final Supplier<AbstractSpellNode> SPELL_EFFECT_BURN =
            SPELL_NODES.register("spell_effect.burn", () -> SpellEffectBurn.INSTANCE);
    public static final Supplier<AbstractSpellNode> SPELL_EFFECT_BREAK =
            SPELL_NODES.register("spell_effect.break", () -> SpellEffectBreak.INSTANCE);

    public static final Supplier<AbstractSpellNode> SPELL_MODIFIER_STRENGTHEN =
            SPELL_NODES.register("spell_modifier.strengthen", () -> SpellModifierStrengthen.INSTANCE);

}
