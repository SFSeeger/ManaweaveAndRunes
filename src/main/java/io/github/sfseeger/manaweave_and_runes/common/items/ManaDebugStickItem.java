package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.mana.Mana;
import io.github.sfseeger.lib.mana.capability.IManaHandler;
import io.github.sfseeger.lib.mana.capability.ManaweaveAndRunesCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ManaDebugStickItem extends Item {
    public ManaDebugStickItem() {
        super(new Item.Properties().rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.PASS;
        }
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        IManaHandler handler =
                level.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK, pos, null);
        if (handler != null) {
            StringBuilder message = new StringBuilder("Current Mana: ");
            for (Mana mana : handler.getManaTypesStored()) {
                message.append(handler.getManaStored(mana)).append(" ").append(mana.getName().getString()).append(" ");
            }
            PlayerChatMessage chatMessage = PlayerChatMessage.unsigned(player.getUUID(), message.toString());
            player.createCommandSourceStack()
                    .sendChatMessage(new OutgoingChatMessage.Player(chatMessage), false,
                                     ChatType.bind(ChatType.CHAT, player));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
