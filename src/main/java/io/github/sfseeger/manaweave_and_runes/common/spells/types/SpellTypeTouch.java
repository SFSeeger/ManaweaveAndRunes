package io.github.sfseeger.manaweave_and_runes.common.spells.types;

import io.github.sfseeger.lib.common.spells.AbstractSpellType;
import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import io.github.sfseeger.lib.common.spells.SpellCastingResult;
import io.github.sfseeger.lib.common.spells.SpellResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class SpellTypeTouch extends AbstractSpellType {
    public static final SpellTypeTouch INSTANCE = new SpellTypeTouch();

    public SpellTypeTouch() {
        super();
    }

    @Override
    public SpellCastingResult cast(AbstractSpellCastingContext context, SpellResolver resolver) {
        return SpellCastingResult.SKIPPED;
    }

    @Override
    public SpellCastingResult castOnBlock(BlockHitResult result, AbstractSpellCastingContext context, SpellResolver resolver) {
        return resolver.resolve(result, context);
    }

    @Override
    public SpellCastingResult castOnEntity(Entity target, AbstractSpellCastingContext context, SpellResolver resolver) {
        return resolver.resolve(new EntityHitResult(target), context);
    }
}
