package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSpellType extends AbstractSpellNode {
    public AbstractSpellType() {
        super();
    }

    public abstract SpellCastingResult cast(SpellCastingContext context, SpellResolver resolver);

    public abstract SpellCastingResult castOnBlock(BlockHitResult result, SpellCastingContext context, SpellResolver resolver);

    public abstract SpellCastingResult castOnEntity(Entity target, SpellCastingContext context, SpellResolver resolver);

    @Override
    public @NotNull SpellNodeType getSpellNodeType() {
        return SpellNodeType.TYPE;
    }
}
