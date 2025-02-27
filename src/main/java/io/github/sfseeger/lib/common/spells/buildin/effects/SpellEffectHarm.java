package io.github.sfseeger.lib.common.spells.buildin.effects;

import io.github.sfseeger.lib.common.datamaps.BlockHarmDataMap;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierDelicate;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SpellEffectHarm extends AbstractSpellEffect {
    public static final SpellEffectHarm INSTANCE = new SpellEffectHarm();
    public SpellEffectHarm() {
        super(Map.of(Manas.SoulMana, 5, Manas.VoidMana, 1), 4);
    }

    @Override
    public SpellCastingResult resolveBlock(BlockHitResult blockHitResult, SpellCastingContext context) {
        Level level = context.getLevel();
        BlockPos pos = blockHitResult.getBlockPos();
        float strength = (float) context.getVariable("strength");

        boolean delicate = (boolean) context.getVariable("delicate");
        if (delicate) return SpellCastingResult.SUCCESS;

        if (SpellUtils.canChangeBlockState(pos, context)) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            Optional<Block> replacement = BlockHarmDataMap.getConvertedBlock(block, level.getRandom(), strength);
            if (replacement.isPresent()) {
                level.setBlockAndUpdate(blockHitResult.getBlockPos(), replacement.get().defaultBlockState());
                return SpellCastingResult.SUCCESS;
            }
        }
        // Return Success to punish missing
        return SpellCastingResult.SUCCESS;
    }

    @Override
    public SpellCastingResult resolveEntity(EntityHitResult entityHitResult, SpellCastingContext context) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            float strength = (float) context.getVariable("strength");
            return entity.hurt(context.getCaster().damageSources().magic(),
                               1 * strength) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
        }
        return SpellCastingResult.SKIPPED;
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of(SpellModifierStrengthen.INSTANCE, SpellModifierDelicate.INSTANCE);
    }
}
