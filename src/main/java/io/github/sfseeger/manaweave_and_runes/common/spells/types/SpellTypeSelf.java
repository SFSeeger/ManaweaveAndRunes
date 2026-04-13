package io.github.sfseeger.manaweave_and_runes.common.spells.types;

import io.github.sfseeger.lib.common.spells.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class SpellTypeSelf extends AbstractSpellType {
    public static final SpellTypeSelf INSTANCE = new SpellTypeSelf();

    public SpellTypeSelf() {
        super();
    }

    @Override
    public SpellCastingResult cast(AbstractSpellCastingContext context, SpellResolver resolver) {
        if (context.getCaster() == null) return SpellCastingResult.SKIPPED;
        return resolver.resolve(new EntityHitResult(context.getCaster()), context);
    }

    @Override
    public SpellCastingResult castOnBlock(BlockHitResult result, AbstractSpellCastingContext context, SpellResolver resolver) {
        if (context.getCaster() == null) return SpellCastingResult.SKIPPED;
        return resolver.resolve(new EntityHitResult(context.getCaster()), context);
    }

    @Override
    public SpellCastingResult castOnEntity(Entity target, AbstractSpellCastingContext context, SpellResolver resolver) {
        if (context.getCaster() == null) return SpellCastingResult.SKIPPED;
        return resolver.resolve(new EntityHitResult(context.getCaster()), context);
    }
}
