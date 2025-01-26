package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.spells.AbstractCaster;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class ManaWeaversWandItem extends Item {
    public ManaWeaversWandItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
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
