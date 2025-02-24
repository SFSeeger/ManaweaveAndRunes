package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.items.IItemHandlerItem;
import io.github.sfseeger.lib.common.items.SpellHolderItem;
import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.manaweave_and_runes.common.data_components.ItemStackHandlerDataComponent;
import io.github.sfseeger.manaweave_and_runes.common.data_components.SelectedSlotDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class ManaWeaversStaffItem extends Item implements IItemHandlerItem, ISpellCaster, IUpgradable {
    public ManaWeaversStaffItem() {
        super(new Item.Properties().stacksTo(1)
                      .rarity(Rarity.EPIC)
                      .component(ManaweaveAndRunesDataComponentsInit.SELECTED_SLOT_DATA_COMPONENT,
                                 new SelectedSlotDataComponent(0)));
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
    public void switchSpell(ItemStack stack, int spellIndex) {
        stack.set(ManaweaveAndRunesDataComponentsInit.SELECTED_SLOT_DATA_COMPONENT,
                  new SelectedSlotDataComponent(getNextSpellIndex(stack, spellIndex)));
    }

    @Override
    public int getCurrentSpellIndex(ItemStack stack) {
        return stack.getOrDefault(ManaweaveAndRunesDataComponentsInit.SELECTED_SLOT_DATA_COMPONENT,
                                  new SelectedSlotDataComponent(0)).selectedSlot();
    }

    @Override
    public Spell getCurrrntSpell(ItemStack stack) {
        return getSpell(stack, getCurrentSpellIndex(stack));
    }

    @Override
    public Spell getSpell(ItemStack stack, int index) {
        ItemStack spellItem = getItemHandler(stack).getStackInSlot(index);
        if (spellItem.getItem() instanceof SpellHolderItem) {
            return SpellHolderItem.getSpell(spellItem);
        }
        return null;
    }

    public int getNextSpellIndex(ItemStack stack, int targetIndex) {
        int slotCount = getSlotCount();
        IItemHandler itemHandler = getItemHandler(stack);

        for (int i = 0; i < slotCount; i++) {
            int nextIndex = (targetIndex + i) % slotCount;
            if (!itemHandler.getStackInSlot(nextIndex).isEmpty()) {
                stack.set(ManaweaveAndRunesDataComponentsInit.SELECTED_SLOT_DATA_COMPONENT,
                          new SelectedSlotDataComponent(nextIndex));

                return nextIndex;
            }
        }
        return 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        SpellCaster caster = new SpellCaster();
        Spell spell = getCurrrntSpell(itemstack);
        if (spell != null) {
            SpellCastingResult result = caster.cast(level, player, hand, spell);
            if (result.isSuccess() && !(result == SpellCastingResult.SKIPPED)) {
                player.getCooldowns().addCooldown(this, spell.getCooldown());
            }
            return result.returnForResult(itemstack);
        }

        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        Spell spell = getCurrrntSpell(stack);
        if (spell != null) {
            tooltipComponents.add(Component.literal(spell.getName()));
        }
    }
}
