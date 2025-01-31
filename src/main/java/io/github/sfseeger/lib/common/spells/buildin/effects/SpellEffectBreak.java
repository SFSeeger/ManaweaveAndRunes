package io.github.sfseeger.lib.common.spells.buildin.effects;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.AbstractSpellEffect;
import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import io.github.sfseeger.lib.common.spells.SpellCastingResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Map;
import java.util.Set;

public class SpellEffectBreak extends AbstractSpellEffect {
    public static final SpellEffectBreak INSTANCE = new SpellEffectBreak();

    public SpellEffectBreak() {
        super(Map.of(Manas.AirMana, 5), 0);
    }

    @Override
    public SpellCastingResult resolveBlock(BlockHitResult blockHitResult, SpellCastingContext context) {
        return breakBlock(blockHitResult.getBlockPos(), context) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
    }

    @Override
    public SpellCastingResult resolveEntity(EntityHitResult entityHitResult, SpellCastingContext context) {
        return breakBlock(entityHitResult.getEntity().getOnPos(), context)
                ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
    }

    private boolean breakBlock(BlockPos pos, SpellCastingContext context) {
        return context.getLevel().destroyBlock(pos, true, context.getCaster());
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of();
    }
}
