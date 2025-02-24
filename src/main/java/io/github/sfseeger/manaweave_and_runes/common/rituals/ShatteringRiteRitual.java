package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PositionRitualData;
import io.github.sfseeger.lib.common.spells.SpellUtils;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
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
import org.jetbrains.annotations.NotNull;

public class ShatteringRiteRitual extends Ritual {
    private static final int MAX_TRIES = 10;

    public ShatteringRiteRitual() {
        super(Tier.NOVICE, -1);
    }

    private static @NotNull BlockPos getRandomBlockPos(BlockPos pos, RandomSource random, Vec3 area) {
        return pos
                .offset(random.nextInt((int) area.x()) - (int) area.x() / 2,
                        random.nextInt((int) area.y()) - ((int) area.y() / 2 + random.nextInt((int) area.y())),
                        random.nextInt((int) area.z()) - (int) area.z() / 2);
    }

    @Override
    public Vec3 getDimension() {
        return new Vec3(16, 16, 16);
    }

    @Override
    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {

        PositionRitualData data = context.getData(RitualDataTypes.POSITION_TYPE);

        if (data == null || data.getPos().distManhattan(pos) >= 64) {
            RitualUtils.displayMessageToStartingPlayer(
                    Component.translatable("ritual.shattering_rite.invalid_position"), level, context);
            return RitualStepResult.ABORT;
        }
        if (level.random.nextInt(100) < 50) {
            return RitualStepResult.SKIP;
        }

        Vec3 d = getDimension();

        RandomSource random = level.random;

        for (int i = 0; i < MAX_TRIES; i++) {
            BlockPos targetPos = getRandomBlockPos(data.getPos(), random, d);
            if (tryBreak(targetPos, level, pos)) {
                return RitualStepResult.SUCCESS;
            }
        }

        return RitualStepResult.SKIP;
    }

    @Override
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        returnPositionRune(level, pos);
    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        returnPositionRune(level, pos);
        tryBreak(getRandomBlockPos(pos, level.random, getDimension()), level, pos);
    }

    private void returnPositionRune(Level level, BlockPos pos) {
        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
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
