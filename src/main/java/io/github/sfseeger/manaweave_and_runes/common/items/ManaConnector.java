package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.manaweave_and_runes.common.data_components.BlockPosDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class ManaConnector extends Item {
    public static final int LINKING_RANGE = 16;
    private static final int ERROR_COLOR = 0xC72222;
    private static final int SUCCESS_COLOR = 0x22C722;

    public ManaConnector() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player p = context.getPlayer();
        BlockPosDataComponent component = stack.get(MRDataComponentsInit.BLOCK_POS_DATA_COMPONENT);

        if (p == null) return InteractionResult.FAIL;

        Optional<IManaNetworkSubscriber> subscriber = getManaNetworkNode(level, pos);
        if (subscriber.isPresent()) {
            if (component == null) {
                if (p.isShiftKeyDown()) {
                    subscriber.get().getManaNetworkNode().disconnectAllNodes();
                    subscriber.get().getManaNetworkNode().setManaNetwork(null);
                    p.displayClientMessage(
                            Component.translatable("item.manaweave_and_runes.mana_connector.unlinking_all")
                                    .withColor(SUCCESS_COLOR), true);
                    return InteractionResult.SUCCESS;
                } else {
                    stack.set(MRDataComponentsInit.BLOCK_POS_DATA_COMPONENT, new BlockPosDataComponent(pos));
                    p.displayClientMessage(
                            Component.translatable("item.manaweave_and_runes.mana_connector.linking_started")
                                    .withColor(SUCCESS_COLOR), true);
                    return InteractionResult.SUCCESS;
                }

            } else {
                BlockPos targetPos = component.pos();
                Optional<IManaNetworkSubscriber> target = getManaNetworkNode(level, targetPos);
                if (target.isPresent()) {
                    if (p.isShiftKeyDown()) {
                        subscriber.get().getManaNetworkNode().disconnectNode(target.get().getManaNetworkNode());
                        p.displayClientMessage(
                                Component.translatable("item.manaweave_and_runes.mana_connector.unlinked")
                                        .withColor(SUCCESS_COLOR), true);
                        return InteractionResult.SUCCESS;
                    } else if (!targetPos.equals(pos) && component.pos().distManhattan(pos) <= LINKING_RANGE) {
                        subscriber.get().getManaNetworkNode().connectNode(target.get().getManaNetworkNode());
                        p.displayClientMessage(Component.translatable("item.manaweave_and_runes.mana_connector.linked")
                                                       .withColor(SUCCESS_COLOR), true);
                        return InteractionResult.SUCCESS;
                    }
                }
                p.displayClientMessage(Component.translatable("item.manaweave_and_runes.mana_connector.invalid_target")
                                               .withColor(ERROR_COLOR), true);
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!isSelected && stack.get(MRDataComponentsInit.BLOCK_POS_DATA_COMPONENT) != null) {
            stack.remove(MRDataComponentsInit.BLOCK_POS_DATA_COMPONENT);
            if (entity instanceof Player player) {
                player.displayClientMessage(
                        Component.translatable("item.manaweave_and_runes.mana_connector.linking_ended")
                                .withColor(ERROR_COLOR), true);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        BlockPosDataComponent component = stack.get(MRDataComponentsInit.BLOCK_POS_DATA_COMPONENT);
        if (component != null) {
            tooltipComponents.add(Component.translatable("item.manaweave_and_runes.mana_connector.target")
                                          .append(": ")
                                          .append(component.pos().toShortString()));
        }
    }

    private Optional<IManaNetworkSubscriber> getManaNetworkNode(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof IManaNetworkSubscriber subscriber) {
            return Optional.of(subscriber);
        } else {
            return Optional.empty();
        }
    }

    /*
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
     */
}
