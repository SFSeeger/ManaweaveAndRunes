package io.github.sfseeger.lib.common.spells.buildin.effects;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.AbstractSpellEffect;
import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import io.github.sfseeger.lib.common.spells.SpellCastingResult;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.Map;
import java.util.Set;

public class SpellEffectBurn extends AbstractSpellEffect {
    public static final SpellEffectBurn INSTANCE = new SpellEffectBurn();

    public SpellEffectBurn() {
        super(Map.of(Manas.FireMana, 5), 5);
    }

    @Override
    public SpellCastingResult resolveBlock(BlockHitResult blockHitResult, SpellCastingContext context) {
        Level level = context.getLevel();
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        InteractionHand hand = context.getCaster().getUsedItemHand();
        UseOnContext useOnContext =
                new UseOnContext(context.getLevel(), null, hand, context.getCaster().getItemInHand(hand),
                                 blockHitResult);
        BlockState modifiedState = state.getToolModifiedState(useOnContext, ItemAbilities.FIRESTARTER_LIGHT, false);
        if (modifiedState == null) {
            BlockPos newPos = pos.relative(blockHitResult.getDirection());
            if (BaseFireBlock.canBePlacedAt(level, newPos, blockHitResult.getDirection().getOpposite())) {
                BlockState blockstate1 = BaseFireBlock.getState(level, newPos);
                level.setBlock(newPos, blockstate1, 11);
                return SpellCastingResult.SUCCESS;
            }
            return SpellCastingResult.FAILURE;
        }
        level.setBlock(pos, modifiedState, 11);
        level.gameEvent(context.getCaster(), GameEvent.BLOCK_CHANGE, pos);
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public SpellCastingResult resolveEntity(EntityHitResult entityHitResult, SpellCastingContext context) {
        entityHitResult.getEntity().setRemainingFireTicks((int) (30 * (float) context.getVariable("strength")));
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of(SpellModifierStrengthen.INSTANCE);
    }
}
