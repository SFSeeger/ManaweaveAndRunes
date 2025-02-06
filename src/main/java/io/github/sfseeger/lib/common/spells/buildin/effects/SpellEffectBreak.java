package io.github.sfseeger.lib.common.spells.buildin.effects;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.spells.AbstractSpellEffect;
import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.lib.common.spells.SpellCastingContext;
import io.github.sfseeger.lib.common.spells.SpellCastingResult;
import io.github.sfseeger.lib.common.spells.buildin.modifiers.SpellModifierStrengthen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
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
        Level level = context.getLevel();
        BlockState state = level.getBlockState(pos);
        float d = state.getDestroySpeed(level, pos);
        float strength = (float) context.getVariable("strength");
        float strengthThreshold = (strength / 4.0f) * 100.0f;

        if (d < 0) {
            return false;
        }
        if (d > strengthThreshold) {
            return false;
        }
        if (!level.getWorldBorder().isWithinBounds(pos)) {
            return false;
        }

        return level.destroyBlock(pos, isHarvestable(state, strength), context.getCaster());
    }

    private Tier getToolTier(float strength) {
        if (strength <= 0.5) {
            return Tiers.WOOD;
        } else if (strength <= 1) {
            return Tiers.STONE;
        } else if (strength <= 2) {
            return Tiers.IRON;
        } else if (strength <= 3) {
            return Tiers.DIAMOND;
        } else if (strength <= 4) {
            return Tiers.NETHERITE;
        } else {
            return Tiers.STONE;
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

    @Override
    public Set<AbstractSpellNode> getPossibleModifiers() {
        return Set.of(SpellModifierStrengthen.INSTANCE);
    }
}
