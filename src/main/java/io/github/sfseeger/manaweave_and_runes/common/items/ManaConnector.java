package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.manaweave_and_runes.common.data_components.BlockPosDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ManaConnector extends Item {
    public ManaConnector() {
        super(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player p = context.getPlayer();
        BlockPosDataComponent component = stack.get(MRDataComponentsInit.BLOCK_POS_DATA_COMPONENT);

        if (p == null) return InteractionResult.FAIL;

        if (p.isShiftKeyDown()) {
            if (level.getBlockEntity(pos) instanceof IManaNetworkSubscriber) {
                stack.set(MRDataComponentsInit.BLOCK_POS_DATA_COMPONENT, new BlockPosDataComponent(pos));
                return InteractionResult.SUCCESS;
            }
        } else if (component != null && !component.pos().equals(pos) && component.pos().distManhattan(pos) <= 16) {
            if (level.getBlockEntity(pos) instanceof IManaNetworkSubscriber subscriber && level.getBlockEntity(component.pos()) instanceof IManaNetworkSubscriber connected) {
                subscriber.getManaNetworkNode().connectNode(connected.getManaNetworkNode());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
