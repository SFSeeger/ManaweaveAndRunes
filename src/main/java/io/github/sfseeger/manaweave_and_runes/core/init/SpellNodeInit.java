package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.lib.common.spells.buildin.effects.*;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierDelicate;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierElongate;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierWiden;
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
    public static final Supplier<AbstractSpellNode> SPELL_EFFECT_HARM =
            SPELL_NODES.register("spell_effect.harm", () -> SpellEffectHarm.INSTANCE);
    public static final Supplier<AbstractSpellNode> SPELL_EFFECT_HEAL =
            SPELL_NODES.register("spell_effect.heal", () -> SpellEffectHeal.INSTANCE);
    public static final Supplier<AbstractSpellNode> SPELL_EFFECT_EXPLODE =
            SPELL_NODES.register("spell_effect.explode", () -> SpellEffectExplode.INSTANCE);
    public static final Supplier<AbstractSpellNode> SPELL_EFFECT_PUSH =
            SPELL_NODES.register("spell_effect.push", () -> SpellEffectPush.INSTANCE);


    public static final Supplier<AbstractSpellNode> SPELL_MODIFIER_STRENGTHEN =
            SPELL_NODES.register("spell_modifier.strengthen", () -> SpellModifierStrengthen.INSTANCE);
    public static final Supplier<AbstractSpellNode> SPELL_MODIFIER_WIDEN =
            SPELL_NODES.register("spell_modifier.widen", () -> SpellModifierWiden.INSTANCE);
    public static final Supplier<AbstractSpellNode> SPELL_MODIFIER_ELONGATE =
            SPELL_NODES.register("spell_modifier.elongate", () -> SpellModifierElongate.INSTANCE);
    public static final Supplier<AbstractSpellNode> SPELL_MODIFIER_DELICATE =
            SPELL_NODES.register("spell_modifier.delicate", () -> SpellModifierDelicate.INSTANCE);

}
