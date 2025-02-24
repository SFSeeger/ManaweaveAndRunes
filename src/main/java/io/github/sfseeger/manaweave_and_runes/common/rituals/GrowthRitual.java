package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrowthRitual extends Ritual {
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
        if (ticksPassed % 20 + level.random.nextInt(4 * 20) != 0) {
            return RitualStepResult.SKIP;
        }

        Vec3 dimension = getDimension();

        List<BlockPos> possibleBlocks = new ArrayList<>();

        // Collect all valid blocks within radius
        for (int x = (int) -dimension.x() / 2; x <= (int) dimension.x() / 2; x++) {
            for (int z = (int) -dimension.z() / 2; z <= (int) dimension.z() / 2; z++) {
                for (int y = (int) -dimension.y() / 2; y <= (int) dimension.y() / 2; y++) {
                    BlockPos pos1 = pos.offset(x, y, z);
                    BlockState targetState = level.getBlockState(pos1);

                    if (targetState.is(BlockTags.BEE_GROWABLES) || targetState.is(BlockTags.SAPLINGS)) {
                        possibleBlocks.add(pos1);
                    }
                }
            }
        }

        RandomSource random = level.random;
        // Shuffle the list and pick a random subset
        Collections.shuffle(possibleBlocks);
        System.out.println("Possible blocks: " + possibleBlocks.size());
        int count = Math.min(3, possibleBlocks.size());

        for (int i = 0; i < count; i++) {
            BlockPos pos1 = possibleBlocks.get(i);
            BlockState targetState = level.getBlockState(pos1);

            if (targetState.getBlock() instanceof CropBlock growable) {
                growable.growCrops(level, pos1, targetState);
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos1.getX(), pos1.getY(), pos1.getZ(), 2, 0, 0.25, 0,
                                    0);
            } else if (targetState.getBlock() instanceof SaplingBlock sapling) {
                sapling.advanceTree(level, pos1, targetState, random);
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos1.getX(), pos1.getY(), pos1.getZ(), 2, 0, 0.25, 0,
                                    0);
            }
        }

        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {

    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {

    }
}
