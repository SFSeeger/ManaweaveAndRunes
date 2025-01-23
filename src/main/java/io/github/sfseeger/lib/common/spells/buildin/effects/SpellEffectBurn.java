package io.github.sfseeger.lib.common.spells.buildin.effects;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.AbstractSpellEffect;
import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import io.github.sfseeger.lib.common.spells.SpellCastingResult;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Map;
import java.util.Set;

public class SpellEffectBurn extends AbstractSpellEffect {
    public SpellEffectBurn() {
        super(Map.of(Manas.FireMana, 5), 5);
    }

    @Override
    public SpellCastingResult resolveBlock(BlockHitResult blockHitResult, SpellCastingContext context) {
        Level level = context.getLevel();
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        //TODO: Replace with flint and steel code
        if (blockHitResult.getDirection().getAxis().isHorizontal()){
            if(state.is(BlockTags.CAMPFIRES) || state.is(BlockTags.CANDLES)){
                level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
            }
            else {
                pos = pos.relative(blockHitResult.getDirection().getOpposite());
                if(BaseFireBlock.canBePlacedAt(level, pos, blockHitResult.getDirection())) {
                    BlockState blockstate1 = BaseFireBlock.getState(level, pos);
                    level.setBlock(pos, blockstate1, 11);
                }
                return SpellCastingResult.FAILURE;
            }
            return SpellCastingResult.SUCCESS;
        }
        pos = pos.above();
        level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
        return SpellCastingResult.FAILURE;
    }

    @Override
    public SpellCastingResult resolveEntity(EntityHitResult entityHitResult, SpellCastingContext context) {
        entityHitResult.getEntity().setRemainingFireTicks((int)(20 * (float)context.getVariable("strength")));
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of();
    }
}
