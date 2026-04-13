package io.github.sfseeger.manaweave_and_runes.common.spells.effects;

import io.github.sfseeger.lib.common.spells.AbstractSpellEffect;
import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import io.github.sfseeger.lib.common.spells.SpellCastingResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.NotNull;

public class SpellEffectBurn extends AbstractSpellEffect {
    public static final SpellEffectBurn INSTANCE = new SpellEffectBurn();

    public SpellEffectBurn() {
        super();
    }

    @Override
    public @NotNull SpellCastingResult resolveBlock(BlockHitResult blockHitResult, AbstractSpellCastingContext context) {
        Level level = context.getLevel();
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        UseOnContext useOnContext = new UseOnContext(context.getLevel(), null, InteractionHand.MAIN_HAND,
                                                     new ItemStack(Items.FLINT_AND_STEEL), blockHitResult);
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
    public @NotNull SpellCastingResult resolveEntity(EntityHitResult entityHitResult, AbstractSpellCastingContext context) {
        entityHitResult.getEntity().igniteForSeconds(2 * context.getFloatContextData("strength", 1f));
        return SpellCastingResult.SUCCESS;
    }
}
