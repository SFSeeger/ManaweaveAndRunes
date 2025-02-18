package io.github.sfseeger.lib.common.spells.buildin.effects;

import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Map;
import java.util.Set;

public class SpellEffectHarm extends AbstractSpellEffect {
    public static final SpellEffectHarm INSTANCE = new SpellEffectHarm();

    private static final Map<Block, Block> CONVERSION_MAP = Map.of(
            Blocks.STONE, Blocks.COBBLESTONE,
            Blocks.COBBLESTONE, Blocks.GRAVEL,
            Blocks.GRAVEL, Blocks.SAND,
            Blocks.SAND, Blocks.CLAY,
            Blocks.DIRT, Blocks.CLAY
    );

    public SpellEffectHarm() {
        super(Map.of(), 4);
    }

    @Override
    public SpellCastingResult resolveBlock(BlockHitResult blockHitResult, SpellCastingContext context) {
        Level level = context.getLevel();
        BlockPos pos = blockHitResult.getBlockPos();
        if (SpellUtils.canChangeBlockState(pos, context)) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            Block replacement = CONVERSION_MAP.get(block);
            if (replacement != null) {
                level.setBlockAndUpdate(blockHitResult.getBlockPos(), replacement.defaultBlockState());
                return SpellCastingResult.SUCCESS;
            }
        }
        // Return Success to punish missing
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public SpellCastingResult resolveEntity(EntityHitResult entityHitResult, SpellCastingContext context) {
        Entity entity = entityHitResult.getEntity();
        float strength = (float) context.getVariable("strength");
        return entity.hurt(context.getCaster().damageSources().magic(),
                           1 * strength) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of(SpellModifierStrengthen.INSTANCE);
    }
}
