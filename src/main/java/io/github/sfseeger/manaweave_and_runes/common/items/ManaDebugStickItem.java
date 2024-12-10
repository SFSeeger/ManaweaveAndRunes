package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.mana.Mana;
import io.github.sfseeger.lib.mana.capability.IManaHandler;
import io.github.sfseeger.lib.mana.capability.ManaweaveAndRunesCapabilities;
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
        IManaHandler handler =
                level.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK, pos, null);
        if (handler != null) {
            MutableComponent message = Component.literal("Current Mana: ");

            for (Mana mana : handler.getManaTypesStored()) {
                message.append(Integer.toString(handler.getManaStored(mana))).append(" ")
                        .append(mana.getName()
                                        .setStyle(Style.EMPTY.withColor(mana.properties().getColor()).withItalic(true)))
                        .append(", ");
            }
            player.sendSystemMessage(message);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
