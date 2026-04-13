package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.context_data_types.IContextDataCapable;
import io.github.sfseeger.lib.common.context_data_types.builtin.PositionContextDataType;
import io.github.sfseeger.manaweave_and_runes.common.data_components.BlockPosDataComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

import static io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit.BLOCK_POS_DATA_COMPONENT;

public class PositionRuneItem extends Item implements IContextDataCapable {
    public PositionRuneItem() {
        super(new Item.Properties().stacksTo(1)
                      .rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        stack.set(BLOCK_POS_DATA_COMPONENT, new BlockPosDataComponent(pos));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        PositionContextDataType data = getData(stack);
        if (data != null) {
            tooltipComponents.add(Component.literal(
                    "Position: " + data.pos().getX() + ", " + data.pos().getY() + ", " + data.pos().getZ()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PositionContextDataType getData(ItemStack stack) {
        BlockPosDataComponent component = stack.get(BLOCK_POS_DATA_COMPONENT);
        if (component != null) {
            return new PositionContextDataType(component.pos());
        }
        return null;
    }
}
