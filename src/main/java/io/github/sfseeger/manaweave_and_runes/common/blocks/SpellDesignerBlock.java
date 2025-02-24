package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.manaweave_and_runes.common.blockentities.RunePedestalBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.SpellDesignerBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.WandModificationTableBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.menus.SpellDesignerMenu;
import io.github.sfseeger.manaweave_and_runes.common.menus.WandModificationTableMenu;
import io.github.sfseeger.manaweave_and_runes.core.util.InventoryUtil;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SpellDesignerBlock extends Block implements EntityBlock {
    public SpellDesignerBlock() {
        super(Properties.of().requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SpellDesignerBlockEntity(blockPos, blockState);
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
        if (level.getBlockEntity(pos) instanceof SpellDesignerBlockEntity entity) {
            return new SimpleMenuProvider(
                    (id, playerInventory, player) -> new SpellDesignerMenu(id, playerInventory, entity,
                            ContainerLevelAccess.create(level,
                                    pos)),
                    Component.translatable("container.manaweave_and_runes.spell_designer"));
        }
        return null;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        InventoryUtil.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
