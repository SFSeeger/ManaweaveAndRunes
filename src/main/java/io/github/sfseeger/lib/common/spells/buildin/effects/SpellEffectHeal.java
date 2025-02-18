package io.github.sfseeger.lib.common.spells.buildin.effects;

import io.github.sfseeger.lib.common.datamaps.BlockHealDataMap;
import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.*;
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

public class SpellEffectHeal extends AbstractSpellEffect {
    public static final SpellEffectHeal INSTANCE = new SpellEffectHeal();

    public SpellEffectHeal() {
        super(Map.of(Manas.AirMana, 10), 10);
    }

    @Override
    public SpellCastingResult resolveBlock(BlockHitResult blockHitResult, SpellCastingContext context) {
        Level level = context.getLevel();
        BlockPos pos = blockHitResult.getBlockPos();
        float strength = (float) context.getVariable("strength");
        if (SpellUtils.canChangeBlockState(pos, context)) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            Optional<Block> replacement = BlockHealDataMap.getConvertedBlock(block, level.getRandom(), strength);
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
            livingEntity.heal(1 * strength);
            return SpellCastingResult.SUCCESS;
        }
        return SpellCastingResult.SKIPPED;
    }

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of(SpellModifierStrengthen.INSTANCE);
    }
}
