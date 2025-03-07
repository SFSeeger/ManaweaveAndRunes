package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.common.blocks.ManaNetworkBlock;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaStorageBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.menus.ManaStorageBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ManaStorageBlock extends ManaNetworkBlock implements EntityBlock {
    public ManaStorageBlock() {
        super(Properties.of()); // TODO: Add properties
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaStorageBlockEntity(blockPos, blockState);
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
        if (level.getBlockEntity(pos) instanceof ManaStorageBlockEntity entity) {
            return new SimpleMenuProvider(
                    (id, playerInventory, player) -> new ManaStorageBlockMenu(id, playerInventory, entity,
                                                                              ContainerLevelAccess.create(level, pos)),
                    Component.translatable("container.manaweave_and_runes.mana_storage_block"));
        }
        return null;
    }
}
