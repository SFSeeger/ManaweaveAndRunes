package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.manaweave_and_runes.client.particles.mana_particle.ManaParticleOptions;
import io.github.sfseeger.manaweave_and_runes.core.util.ParticleUtils;
import io.github.sfseeger.manaweave_and_runes.core.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class GrowthRitual extends Ritual {
    private static final int MAX_TRIES_PER_TICK = 15;
    private static final int MAX_ACCELERATED_BLOCKS = 3;

    public GrowthRitual() {
        super(Tier.NOVICE, -1);
    }

    @Override
    public Vec3 getDimension() {
        return new Vec3(30, 30, 30);
    }

    @Override
    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {

        Vec3 dimension = getDimension();
        RandomSource random = level.random;

        int blocksAccelerated = 0;

        for (int i = 0; i < MAX_TRIES_PER_TICK; i++) {
            BlockPos pos1 = Utils.getRandomBlockPos(pos, random, dimension);
            BlockState targetState = level.getBlockState(pos1);
            if (blocksAccelerated >= MAX_ACCELERATED_BLOCKS) {
                break;
            }

            if (targetState.is(BlockTags.BEE_GROWABLES) || targetState.is(BlockTags.SAPLINGS)) {
                if (targetState.getBlock() instanceof CropBlock growable) {
                    growable.growCrops(level, pos1, targetState);
                    level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos1.getX(), pos1.getY(), pos1.getZ(), 2, 0, 0.25,
                                        0,
                                        0);
                } else if (targetState.getBlock() instanceof SaplingBlock sapling) {
                    sapling.advanceTree(level, pos1, targetState, random);
                    level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos1.getX(), pos1.getY(), pos1.getZ(), 2, 0, 0.25,
                                        0,
                                        0);
                }
                blocksAccelerated++;
            }
        }

        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {

    }

    @Override
    public void onRitualClientTick(Level level, BlockPos pos, BlockState state, int ticksPassed, RitualContext context,
            RitualOriginType originType) {
        Vec3 d = getDimension();
        RandomSource random = level.getRandom();

        for (int i = 0; i < 15; i++) {
            Vec3 t = ParticleUtils.randomPosInsideBox(pos, random, -d.x() / 2, -d.y() / 2, -d.z() / 2, d.x() / 2,
                                                      d.y() / 2, d.z() / 2);
            level.addParticle(new ManaParticleOptions(Math.min(random.nextFloat(), 0.5f), 0f, 1f, 0.1f, -0.1f,
                                                      random.nextFloat() + 0.8f), t.x(), t.y(), t.z(), 0, 0, 0);
        }
    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {

    }
}
