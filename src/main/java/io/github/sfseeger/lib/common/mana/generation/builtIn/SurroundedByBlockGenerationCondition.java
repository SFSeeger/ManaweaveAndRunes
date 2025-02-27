package io.github.sfseeger.lib.common.mana.generation.builtIn;

import com.mojang.datafixers.util.Either;
import io.github.sfseeger.lib.common.mana.generation.AbstractManaGenerationCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SurroundedByBlockGenerationCondition extends AbstractManaGenerationCondition {
    private final int manaPerBlock;
    private Either<Block, TagKey<Block>> target;

    public SurroundedByBlockGenerationCondition(Either<Block, TagKey<Block>> target, int manaPerBlock) {
        this.target = target;
        this.manaPerBlock = manaPerBlock;
    }

    public SurroundedByBlockGenerationCondition(Block target, @Nullable Integer manaPerBlock) {
        this(Either.left(target), manaPerBlock == null ? 1 : manaPerBlock);
    }


    public SurroundedByBlockGenerationCondition(TagKey<Block> target, @Nullable Integer manaPerBlock) {
        this(Either.right(target), manaPerBlock == null ? 1 : manaPerBlock);

    }

    public static int countSurroundingBlocks(Level level, BlockPos pos, BlockState state,
            Either<Block, TagKey<Block>> target) {
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos offset = pos.offset(x, -1, z);
                BlockState blockState = level.getBlockState(offset);
                count += target.map(
                        block -> blockState.is(block) ? 1 : 0,
                        tag -> blockState.is(tag) ? 1 : 0
                );
            }
        }
        return count;
    }

    @Override
    public int getManaGenerationPotential(Level level, BlockPos pos, BlockState state) {
        return countSurroundingBlocks(level, pos, state, target) * manaPerBlock;
    }
}
