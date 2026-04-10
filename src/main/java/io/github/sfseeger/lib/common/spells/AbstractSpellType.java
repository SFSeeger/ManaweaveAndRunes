package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class AbstractSpellType extends AbstractSpellNode {
    public AbstractSpellType(Map<Mana, Integer> baseCosts, int baseCooldown) {
        super(baseCosts, baseCooldown);
    }

    public abstract SpellCastingResult cast(SpellCastingContext context, SpellResolver resolver);

    public abstract SpellCastingResult castOnBlock(BlockHitResult result, SpellCastingContext context, SpellResolver resolver);

    public abstract SpellCastingResult castOnEntity(Entity target, SpellCastingContext context, SpellResolver resolver);

    @Override
    public @NotNull SpellNodeType getSpellNodeType() {
        return SpellNodeType.TYPE;
    }
}
