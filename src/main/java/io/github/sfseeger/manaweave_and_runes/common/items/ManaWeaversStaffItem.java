package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.items.IItemHandlerItem;
import io.github.sfseeger.lib.common.items.SpellHolderItem;
import io.github.sfseeger.lib.common.spells.*;
import io.github.sfseeger.manaweave_and_runes.client.renderers.item.ManaWeaversStaffRenderer;
import io.github.sfseeger.manaweave_and_runes.common.data_components.ItemStackHandlerDataComponent;
import io.github.sfseeger.manaweave_and_runes.common.data_components.SelectedSlotDataComponent;
import io.github.sfseeger.manaweave_and_runes.core.init.MRDataComponentsInit;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
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
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.List;
import java.util.function.Consumer;

public class ManaWeaversStaffItem extends Item implements IItemHandlerItem, ISpellCaster, IUpgradable, GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ManaWeaversStaffItem() {
        super(new Item.Properties().stacksTo(1)
                      .rarity(Rarity.EPIC)
                      .component(MRDataComponentsInit.SELECTED_SLOT_DATA_COMPONENT,
                                 new SelectedSlotDataComponent(0)));
    }

    @Override
    public IItemHandler getItemHandler(ItemStack stack) {
        if (!stack.has(MRDataComponentsInit.ITEM_STACK_HANDLER_DATA_COMPONENT)) {
            stack.set(MRDataComponentsInit.ITEM_STACK_HANDLER_DATA_COMPONENT.get(),
                      new ItemStackHandlerDataComponent(new ItemStackHandler(getSlotCount())));
        }

        return stack.get(MRDataComponentsInit.ITEM_STACK_HANDLER_DATA_COMPONENT).getItemHandler();
    }

    @Override
    public int getSlotCount() {
        return 5;
    }

    @Override
    public void switchSpell(ItemStack stack, int spellIndex) {
        stack.set(MRDataComponentsInit.SELECTED_SLOT_DATA_COMPONENT,
                  new SelectedSlotDataComponent(getNextSpellIndex(stack, spellIndex)));
    }

    @Override
    public int getCurrentSpellIndex(ItemStack stack) {
        return stack.getOrDefault(MRDataComponentsInit.SELECTED_SLOT_DATA_COMPONENT,
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
                stack.set(MRDataComponentsInit.SELECTED_SLOT_DATA_COMPONENT,
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

    private PlayState idlePredicate(AnimationState<ManaWeaversStaffItem> state) {
        state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::idlePredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private ManaWeaversStaffRenderer renderer;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new ManaWeaversStaffRenderer();

                return this.renderer;
            }
        });
    }
}
