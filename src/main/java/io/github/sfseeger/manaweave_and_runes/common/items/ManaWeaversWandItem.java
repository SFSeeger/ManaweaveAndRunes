package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.items.IItemHandlerItem;
import io.github.sfseeger.lib.common.spells.AbstractCaster;
import io.github.sfseeger.lib.common.spells.ISpellCaster;
import io.github.sfseeger.manaweave_and_runes.common.data_components.ItemStackHandlerDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ManaWeaversWandItem extends Item implements IItemHandlerItem, ISpellCaster {
    public ManaWeaversWandItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public IItemHandler getItemHandler(ItemStack stack) {
        if (!stack.has(ManaweaveAndRunesDataComponentsInit.ITEM_STACK_HANDLER_DATA_COMPONENT)) {
            stack.set(ManaweaveAndRunesDataComponentsInit.ITEM_STACK_HANDLER_DATA_COMPONENT.get(),
                      new ItemStackHandlerDataComponent(new ItemStackHandler(getSlotCount())));
        }

        return stack.get(ManaweaveAndRunesDataComponentsInit.ITEM_STACK_HANDLER_DATA_COMPONENT).getItemHandler();
    }

    @Override
    public int getSlotCount() {
        return 5;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            AbstractCaster caster = new AbstractCaster();
            player.getCooldowns().addCooldown(this, caster.currentSpell.getCooldown());
            caster.cast(level, player, hand, caster.currentSpell).getResult();
        }
        return InteractionResultHolder.pass(itemstack);
    }
}
