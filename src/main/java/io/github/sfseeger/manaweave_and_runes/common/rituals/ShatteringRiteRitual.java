package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.context_data_types.ContextDataTypes;
import io.github.sfseeger.lib.common.context_data_types.builtin.PositionContextDataType;
import io.github.sfseeger.lib.common.spells.SpellUtils;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class ShatteringRiteRitual extends Ritual {
    private static final int MAX_TRIES = 10;

    public ShatteringRiteRitual() {
        super(Tier.NOVICE, -1);
    }


    @Override
    public Vec3 getDimension() {
        return new Vec3(16, 16, 16);
    }

    @Override
    public RitualStepResult onRitualServerTick(RitualStateMachineContext ctx) {

        PositionContextDataType data = ctx.contextMap().getData(ContextDataTypes.POSITION_TYPE).orElse(null);

        if (data == null || data.pos().distManhattan(ctx.pos()) >= 64) {
            RitualUtils.displayMessageToStartingPlayer(
                    Component.translatable("ritual.shattering_rite.invalid_position"), ctx.level(), ctx.contextMap());
            return RitualStepResult.FAIL;
        }
        if (ctx.level().random.nextInt(100) < 50) {
            return RitualStepResult.SKIP;
        }

        Vec3 d = getDimension();

        RandomSource random = ctx.level().random;

        for (int i = 0; i < MAX_TRIES; i++) {
            BlockPos targetPos = Utils.getRandomBlockPos(data.pos(), random, d);
            if (tryBreak(targetPos, ctx.level(), ctx.pos())) {
                return RitualStepResult.SUCCESS;
            }
        }

        return RitualStepResult.SKIP;
    }

    @Override
    public void onRitualEnd(RitualStateMachineContext ctx) {
        returnPositionRune(ctx.level(), ctx.pos());
    }

    @Override
    public void onRitualAbort(Level level, BlockPos pos, BlockState state, ContextMap context,
                              RitualOriginType originType) {
        returnPositionRune(level, pos);
        tryBreak(Utils.getRandomBlockPos(pos, level.random, getDimension()), level, pos);
    }

    private void returnPositionRune(Level level, BlockPos pos) {
        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 4, pos.getZ(),
                                            new ItemStack(MRItemInit.POSITION_RUNE_ITEM.get())));
    }

    private boolean tryBreak(BlockPos pos, Level level, BlockPos dropPlace) {
        BlockState targetState = level.getBlockState(pos);
        if (SpellUtils.canChangeBlockState(pos, level) && targetState.getDestroySpeed(level,
                                                                                      pos) > 0 && !targetState.is(
                BlockTags.AIR)) {
            LootParams.Builder lootParams = new LootParams.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.TOOL, new ItemStack(Items.DIAMOND_PICKAXE));
            level.destroyBlock(pos, false);
            targetState.getDrops(lootParams).forEach((stack) -> {
                Containers.dropItemStack(level, dropPlace.getX(), dropPlace.getY() + 1, dropPlace.getZ(), stack);
            });
            return true;
        }
        return false;
    }
}
