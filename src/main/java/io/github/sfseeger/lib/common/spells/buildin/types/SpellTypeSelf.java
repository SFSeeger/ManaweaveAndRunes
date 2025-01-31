package io.github.sfseeger.lib.common.spells.buildin.types;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.spells.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Map;
import java.util.Set;

public class SpellTypeSelf extends AbstractSpellType {
    public static final SpellTypeSelf INSTANCE = new SpellTypeSelf();

    public SpellTypeSelf() {
        super(Map.of(), 2);
    }

    @Override
    public SpellCastingResult cast(SpellCastingContext context, SpellResolver resolver) {
        return resolver.resolve(new EntityHitResult(context.getCaster()), context);
    }

    @Override
    public SpellCastingResult castOnBlock(BlockHitResult result, SpellCastingContext context, SpellResolver resolver) {
        return resolver.resolve(new EntityHitResult(context.getCaster()), context);
    }

    @Override
    public SpellCastingResult castOnEntity(Entity target, SpellCastingContext context, SpellResolver resolver) {
        return resolver.resolve(new EntityHitResult(context.getCaster()), context);
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of();
    }
}
