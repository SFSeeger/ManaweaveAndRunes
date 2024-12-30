package io.github.sfseeger.lib.common.items;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractRuneItem extends Item implements IManaItem {
    protected Supplier<Mana> manatype;
    public static final int MANA_CAPACITY = 5000;
    public static final int MAX_EXTRACT = 200;
    public static final int MAX_RECEIVE = 400;


    public AbstractRuneItem(Properties properties, Supplier<Mana> manatype) {
        super(properties.stacksTo(1));
        this.manatype = manatype;
    }

    public Mana getManaType() {
        return this.manatype.get();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return isManaBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return getManaBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return getManaBarColor(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        addTooltip(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public int getManaCapacity() {
        return MANA_CAPACITY;
    }

    @Override
    public int getMaxExtract() {
        return MAX_EXTRACT;
    }

    @Override
    public int getMaxReceive() {
        return MAX_RECEIVE;
    }

    public @NotNull String toString() {
        return "Rune{" + this.manatype + "}";
    }
}
