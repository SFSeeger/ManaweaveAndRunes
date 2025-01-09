package io.github.sfseeger.lib.common.blocks;

import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class ManaNetworkBlock extends Block {
    public ManaNetworkBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
            FluidState fluid) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity != null) {
                if (entity instanceof IManaNetworkSubscriber subscriber) {
                    subscriber.removeNode((ServerLevel) level);
                }
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
