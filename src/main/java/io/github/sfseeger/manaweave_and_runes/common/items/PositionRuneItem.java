package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualDataCapable;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PositionRitualData;
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

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit.BLOCK_POS_DATA_COMPONENT;

public class PositionRuneItem extends Item implements IRitualDataCapable {
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
        PositionRitualData data = getData(stack);
        if (data != null) {
            tooltipComponents.add(Component.literal(
                    "Position: " + data.getPos().getX() + ", " + data.getPos().getY() + ", " + data.getPos().getZ()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PositionRitualData getData(ItemStack stack) {
        BlockPosDataComponent component = stack.get(BLOCK_POS_DATA_COMPONENT);
        if (component != null) {
            return new PositionRitualData(component.pos());
        }
        return null;
    }
}
