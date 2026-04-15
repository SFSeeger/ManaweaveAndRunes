package io.github.sfseeger.manaweave_and_runes.common.spells.effects;

import io.github.sfseeger.lib.common.datamaps.BlockHarmDataMap;
import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.manaweave_and_runes.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SpellEffectHarm extends AbstractSpellEffect {
    public static final SpellEffectHarm INSTANCE = new SpellEffectHarm();
    public SpellEffectHarm() {
        super();
    }

    @Override
    public @NotNull SpellCastingResult resolveBlock(BlockHitResult blockHitResult, AbstractSpellCastingContext context) {
        Level level = context.getLevel();
        BlockPos pos = blockHitResult.getBlockPos();
        float strength = context.getFloatContextData("strength", 1f);

        boolean delicate = context.getBooleanContextData("delicate");
        if (delicate) return SpellCastingResult.SUCCESS;

        if (SpellUtils.canChangeBlockState(pos, context) && Config.allowHarmSpellGriefing) {
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
    public @NotNull SpellCastingResult resolveEntity(EntityHitResult entityHitResult, AbstractSpellCastingContext context) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            float strength = (float) context.getFloatContextData("strength", 1f);
            return entity.hurt(context.getCaster().damageSources().magic(),
                               1 * strength) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
        }
        return SpellCastingResult.SKIPPED;
    }
}
