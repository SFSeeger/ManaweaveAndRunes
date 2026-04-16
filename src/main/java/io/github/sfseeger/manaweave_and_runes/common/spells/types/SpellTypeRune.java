package io.github.sfseeger.manaweave_and_runes.common.spells.types;

import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.lib.common.spells.casting_context.BlockCasterCastingContext;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blocks.SpellRuneBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class SpellTypeRune extends AbstractSpellType {
    public static final SpellTypeRune INSTANCE = new SpellTypeRune();

    @Override
    public SpellCastingResult cast(AbstractSpellCastingContext context, SpellResolver resolver) {
        return SpellCastingResult.SKIPPED;
    }

    @Override
    public SpellCastingResult castOnBlock(BlockHitResult result, AbstractSpellCastingContext context, SpellResolver resolver) {
        return placeRune(result.getBlockPos().relative(result.getDirection()), context, resolver);
    }

    @Override
    public SpellCastingResult castOnEntity(Entity target, AbstractSpellCastingContext context, SpellResolver resolver) {
        return placeRune(target.getOnPos().above(), context, resolver);
    }


    private SpellCastingResult placeRune(BlockPos pos, AbstractSpellCastingContext context, SpellResolver resolver) {
        if (!SpellUtils.canChangeBlockState(pos, context)) return SpellCastingResult.SKIPPED;
        Level level = context.getLevel();
        if (level == null) return SpellCastingResult.SKIPPED;

        BlockCasterCastingContext context1 = new BlockCasterCastingContext(pos, new Vec3(0, 1, 0), Direction.UP, level);
        context1.setCaster(context.getCaster());
        context1.setContextData(context.getContextData());

        BlockState state = MRBlockInit.SPELL_RUNE_BLOCK.get().defaultBlockState();

        if (context.getBooleanContextData("delicate")) {
            state.setValue(SpellRuneBlock.HIDDEN, true);
        }

        level.setBlockAndUpdate(pos, state);
        level.getBlockEntity(pos, MRBlockEntityInit.SPELL_RUNE_BLOCK_ENTITY.get()).ifPresent(blockEntity -> {
            ManaweaveAndRunes.LOGGER.debug("Placed spell rune block at {}, setting resolver and context", pos);
            blockEntity.setResolver(resolver);
            blockEntity.setContext(context1);
        });
        return SpellCastingResult.SUCCESS;
    }
}
