package io.github.sfseeger.lib.common.spells;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSpellType extends AbstractSpellNode {
    public AbstractSpellType() {
        super();
    }

    public abstract SpellCastingResult cast(AbstractSpellCastingContext context, SpellResolver resolver);

    public abstract SpellCastingResult castOnBlock(BlockHitResult result, AbstractSpellCastingContext context, SpellResolver resolver);

    public abstract SpellCastingResult castOnEntity(Entity target, AbstractSpellCastingContext context, SpellResolver resolver);

    @Override
    public @NotNull SpellNodeType getSpellNodeType() {
        return SpellNodeType.TYPE;
    }
}
