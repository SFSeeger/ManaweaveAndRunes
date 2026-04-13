package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.LibUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class SpellResolver  implements INBTSerializable<Tag> {
    private Spell spell;

    public SpellResolver(Spell spell) {
        this.spell = spell;
    }

    public SpellCastingResult resolve(UseOnContext useOnContext, AbstractSpellCastingContext context){
        return resolve(new BlockHitResult(
                useOnContext.getClickLocation(),
                useOnContext.getClickedFace(),
                useOnContext.getClickedPos(), false),
                context);
    }

    public SpellCastingResult resolve(HitResult rayTrace, AbstractSpellCastingContext context) {
        AbstractSpellCastingContext localContext = context.intoFreshInstance();
        SpellCastingResult result = spell.resolveEffects(rayTrace, localContext);
        spell.getCore().getModifiers().forEach(modifier -> modifier.postResolve(rayTrace, context));
        return result;
    }

    @Override
    public Tag serializeNBT(HolderLookup.Provider provider) {
        return LibUtils.encode(Spell.CODEC, spell, provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        this.spell = LibUtils.decode(Spell.CODEC, tag, provider);
    }
}
