package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ManaDebugStickItem extends Item {
    public ManaDebugStickItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.PASS;
        }
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        boolean success = false;

        if (level.getBlockEntity(pos) instanceof IManaNetworkSubscriber subscriber) {
            success = true;

            MutableComponent message = Component.literal("Node: ");

            UUID networkId = subscriber.getManaNetworkNode().getNetworkId();

            message.append("Network ID: ").append(networkId != null ? networkId.toString() : "null");
            message.append("\nConnected Nodes: ")
                    .append(Integer.toString(subscriber.getManaNetworkNode().getConnectedNodes().size()));
            message.append("\nPriority: ").append(Integer.toString(subscriber.getManaNetworkNode().getPriority()));
            message.append("\nType: ").append(subscriber.getManaNetworkNode().getNodeType().toString());
            player.sendSystemMessage(message);
        }


        IManaHandler handler =
                level.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK, pos, null);
        if (handler != null) {
            success = true;

            MutableComponent message = Component.literal("Current Mana: ");

            for (Mana mana : handler.getManaTypesStored()) {
                message.append(Integer.toString(handler.getManaStored(mana))).append(" ")
                        .append(mana.getName()
                                        .setStyle(Style.EMPTY.withColor(mana.properties().getColor()).withItalic(true)))
                        .append(", ");
            }
            player.sendSystemMessage(message);
        }
        return success ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
