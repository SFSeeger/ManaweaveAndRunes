package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.common.blocks.ManaNetworkBlock;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RunicLoomBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.menus.RunicLoomMenu;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RunicLoomBlock extends ManaNetworkBlock implements EntityBlock {
    public RunicLoomBlock() {
        super(Properties.of().sound(SoundType.WOOD).strength(2.0f).noOcclusion());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RunicLoomBlockEntity(blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(Objects.requireNonNull(state.getMenuProvider(level, pos)), pos);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos, MRBlockEntityInit.RUNIC_LOOM_BLOCK_ENTITY.get()).isPresent()) {
            return new SimpleMenuProvider(
                    (id, playerInventory, player) -> new RunicLoomMenu(id, playerInventory,
                                                                       level.getBlockEntity(pos,
                                                                                            MRBlockEntityInit.RUNIC_LOOM_BLOCK_ENTITY.get())
                                                                               .orElseThrow(),
                                                                       ContainerLevelAccess.create(level, pos)),
                    Component.translatable("container.manaweave_and_runes.runic_loom"));
        }
        return null;
    }
}
