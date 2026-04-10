package io.github.sfseeger.manaweave_and_runes.common.spells;

import io.github.sfseeger.lib.common.spells.AbstractSpellModifier;
import io.github.sfseeger.lib.common.spells.Spell;
import io.github.sfseeger.lib.common.spells.SpellNodeType;
import io.github.sfseeger.lib.common.spells.SpellPart;
import io.github.sfseeger.lib.common.spells.data_components.SpellDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellAssembler {
    public static ItemStack createSpellItemStack(Spell spell) {
        ItemStack stack = new ItemStack(MRItemInit.SPELL_HOLDER_ITEM.get());
        stack.set(MRDataComponentsInit.SPELL_DATA_COMPONENT, new SpellDataComponent(spell));
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(spell.getName()));
        return stack;
    }

    public static ItemStack createSpellPartItemStack(SpellPart spellPart, @Nullable String name) {
        ItemStack stack = new ItemStack(MRItemInit.SPELL_PART.get());
        stack.set(MRDataComponentsInit.SPELL_PART_DATA_COMPONENT, spellPart);
        if (name != null) stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
        return stack;
    }

    public static @Nullable Spell assambleSpell(SpellPart core, Iterable<SpellPart> outer, @Nullable String name) {
        if (core.getSpellNodeType() != SpellNodeType.TYPE) return null;
        Spell spell = new Spell(core);
        if (name != null) spell.setName(name);
        for (SpellPart part : outer) {
            if (part.getSpellNodeType() != SpellNodeType.EFFECT) return null;
            spell.addSpellPart(part);
        }
        return spell.isValid() ? spell : null;
    }

    public static @Nullable SpellPart assembleSpellPart(SpellPart core, List<SpellPart> outer) {
        core = core.clone();
        if (outer.isEmpty()) return core;
        if (outer.stream().anyMatch(part -> part.getSpellNodeType() != SpellNodeType.MODIFIER)) return null;
        List<AbstractSpellModifier> modifiers = core.getModifiers();
        try {
            outer.stream()
                    .map(SpellPart::getCore)
                    .forEach(modifier -> modifiers.add((AbstractSpellModifier) modifier.value()));
        } catch (ClassCastException e) {
            return null;
        }
        return core.isValid() ? core : null;
    }
}
