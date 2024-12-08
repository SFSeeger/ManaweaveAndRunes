package io.github.sfseeger.manaweave_and_runes.common.blocks;

import io.github.sfseeger.manaweave_and_runes.common.api.capability.IManaHandler;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaStorageBlockEntity;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesManaInit;
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
        if (level.isClientSide) {
            return ItemInteractionResult.CONSUME;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ManaStorageBlockEntity manaStorageBlockEntity) {
            IManaHandler manaHandler = manaStorageBlockEntity.getManaHandler(null);
            if (stack.is(Items.COAL)) {
                int received = manaHandler.receiveMana(1000,
                                                       ManaweaveAndRunesManaInit.FIRE_MANA.get(),
                                                       false);
                if (received > 0) {
                    stack.shrink(1);
                    return ItemInteractionResult.SUCCESS;
                }
                return ItemInteractionResult.FAIL;
            }
            if (stack.is(Items.APPLE)) {
                PlayerChatMessage chatMessage = PlayerChatMessage.unsigned(player.getUUID(),
                                                                           "Current Mana: " + manaHandler
                                                                                   .getManaStored(
                                                                                           ManaweaveAndRunesManaInit.FIRE_MANA.get())
                                                                                   + " "
                                                                                   + ManaweaveAndRunesManaInit.FIRE_MANA.get()
                                                                                   .getName()
                                                                                   .getString());
                player.createCommandSourceStack()
                        .sendChatMessage(new OutgoingChatMessage.Player(chatMessage), false,
                                         ChatType.bind(ChatType.CHAT, player));
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.FAIL;
    }
}
