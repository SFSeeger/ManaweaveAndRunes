package io.github.sfseeger.manaweave_and_runes.common.spells.effects;

import io.github.sfseeger.lib.common.context_data_types.ContextDataTypes;
import io.github.sfseeger.lib.common.context_data_types.builtin.BooleanContextDataType;
import io.github.sfseeger.lib.common.context_data_types.builtin.FloatContextDataType;
import io.github.sfseeger.lib.common.spells.AbstractSpellEffect;
import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import io.github.sfseeger.lib.common.spells.SpellCastingResult;
import io.github.sfseeger.lib.common.spells.SpellUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class SpellEffectBreak extends AbstractSpellEffect {
    public static final SpellEffectBreak INSTANCE = new SpellEffectBreak();

    public SpellEffectBreak() {
        super();
    }

    @Override
    public @NotNull SpellCastingResult resolveBlock(BlockHitResult blockHitResult, AbstractSpellCastingContext context) {
        return breakBlock(blockHitResult.getBlockPos(), context,
                          blockHitResult.getDirection()) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
    }

    @Override
    public @NotNull SpellCastingResult resolveEntity(EntityHitResult entityHitResult, AbstractSpellCastingContext context) {
        return breakBlock(entityHitResult.getEntity().getOnPos(), context,
                          Direction.UP) ? SpellCastingResult.SUCCESS : SpellCastingResult.FAILURE;
    }

    private boolean breakBlock(BlockPos pos, AbstractSpellCastingContext context, Direction direction) {
        Level level = context.getLevel();
        float strength = context.getFloatContextData("strength", 1f);
        boolean delicate = context.getBooleanContextData("delicate");

        BlockState targetState = level.getBlockState(pos);


        return SpellUtils.executeOnPlane(pos, context, direction, (pos1) -> {
            boolean isPlayer = context.getCaster() instanceof Player;

            BlockState state = level.getBlockState(pos1);
            if (delicate && !state.equals(targetState)) return false;

            float d = state.getDestroySpeed(level, pos1);

            float strengthThreshold = (strength / 3.0f) * 100.0f;

            if (d < 0) {
                return false;
            }
            if (d > strengthThreshold) {
                return false;
            }
            if (!SpellUtils.canChangeBlockState(pos1, context)) {
                return false;
            }

            boolean shouldHarvest =
                    isHarvestable(state, strength) && !(isPlayer && ((Player) context.getCaster()).isCreative());
            return level.destroyBlock(pos1, shouldHarvest, context.getCaster());
        });
    }

    private Tier getToolTier(float strength) {
        if (strength <= 1) {
            return Tiers.IRON;
        } else if (strength <= 2) {
            return Tiers.DIAMOND;
        } else if (strength <= 3) {
            return Tiers.NETHERITE;
        } else {
            return Tiers.IRON;
        }
    }

    private boolean isHarvestable(BlockState state, float strength) {
        Tier toolTier = getToolTier(strength);

        // Fake item stack with the correct tool material
        ItemStack fakePickaxe = new ItemStack(Items.IRON_PICKAXE);

        if (toolTier == Tiers.WOOD) fakePickaxe = new ItemStack(Items.WOODEN_PICKAXE);
        if (toolTier == Tiers.STONE) fakePickaxe = new ItemStack(Items.STONE_PICKAXE);
        if (toolTier == Tiers.IRON) fakePickaxe = new ItemStack(Items.IRON_PICKAXE);
        if (toolTier == Tiers.DIAMOND) fakePickaxe = new ItemStack(Items.DIAMOND_PICKAXE);
        if (toolTier == Tiers.NETHERITE) fakePickaxe = new ItemStack(Items.NETHERITE_PICKAXE);

        return !state.requiresCorrectToolForDrops() || fakePickaxe.isCorrectToolForDrops(state);
    }
}
