package io.github.sfseeger.lib.common.items;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class AbstractRuneItem extends Item {
    protected Supplier<Mana> manatype;

    public AbstractRuneItem(Properties properties, Supplier<Mana> manatype) {
        super(properties.stacksTo(1));
        this.manatype = manatype;
    }

    public Mana getManaType() {
        return this.manatype.get();
    }

    public IManaHandler getManaHandler() {
        return null;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        IManaHandler manaHandler = getManaHandlerFromStack(stack);
        return manaHandler != null && manaHandler.getManaStored(getManaType()) > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return getManaType().properties().getColor();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        IManaHandler manaHandler = getManaHandlerFromStack(stack);
        return Math.round((float) manaHandler.getManaStored(getManaType()) / (float) manaHandler.getManaCapacity() * 13.0f);
    }

    public static IManaHandler getManaHandlerFromStack(ItemStack stack) {
        IManaHandler manaHandler = stack.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
        return manaHandler;
    }

    public @NotNull String toString() {
        return "Rune{" + this.manatype + "}";
    }
}
