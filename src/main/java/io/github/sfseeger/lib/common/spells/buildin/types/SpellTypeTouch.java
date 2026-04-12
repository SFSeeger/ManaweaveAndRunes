package io.github.sfseeger.lib.common.spells.buildin.types;

import io.github.sfseeger.lib.common.spells.AbstractSpellType;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
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
    public SpellCastingResult cast(SpellCastingContext context, SpellResolver resolver) {
        return SpellCastingResult.SKIPPED;
    }

    @Override
    public SpellCastingResult castOnBlock(BlockHitResult result, SpellCastingContext context, SpellResolver resolver) {
        return resolver.resolve(result, context);
    }

    @Override
    public SpellCastingResult castOnEntity(Entity target, SpellCastingContext context, SpellResolver resolver) {
        return resolver.resolve(new EntityHitResult(target), context);
    }
}
