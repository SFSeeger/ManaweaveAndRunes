package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.manaweave_and_runes.common.blockentities.RuneCarverBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RuneCarverBlock extends Block implements EntityBlock {
    private static final Component CONTAINER_TITLE =
            Component.translatable("container.manaweave_and_runes.rune_carver_block");

    public RuneCarverBlock() {
        super(Properties.of().strength(2.5f));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(Objects.requireNonNull(state.getMenuProvider(level, pos)), pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RuneCarverBlockEntity runeCarverBlockEntity) {
            return new SimpleMenuProvider(
                    (id, playerInventory, player) -> runeCarverBlockEntity.getMenu(id, playerInventory,
                                                                                   ContainerLevelAccess.create(level,
                                                                                                               pos)),
                    CONTAINER_TITLE);
        }
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RuneCarverBlockEntity(blockPos, blockState);
    }
}
