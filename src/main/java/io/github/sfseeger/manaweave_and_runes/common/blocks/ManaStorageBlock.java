package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.lib.mana.ManaDateComponent;
import io.github.sfseeger.lib.mana.capability.IManaHandler;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaStorageBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ManaStorageBlock extends Block implements EntityBlock {
    public ManaStorageBlock() {
        super(BlockBehaviour.Properties.of());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaStorageBlockEntity(blockPos, blockState);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            ManaDateComponent manaDateComponent = stack.get(ManaweaveAndRunesDataComponentsInit.MANA_DATA_COMPONENT.value());
            if (blockEntity instanceof ManaStorageBlockEntity manaStorageBlockEntity && manaDateComponent != null) {
                IManaHandler manaHandler = manaStorageBlockEntity.getManaHandler(null);
                int received = manaHandler.receiveMana(manaDateComponent.getManaAmount(),
                        manaDateComponent.getManaType().value(),
                        false);
                if (received > 0) {
                    stack.shrink(1);
                    return ItemInteractionResult.SUCCESS;
                }
                return ItemInteractionResult.CONSUME_PARTIAL;

            }
        }
        return ItemInteractionResult.FAIL;
    }
}
