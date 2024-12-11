package io.github.sfseeger.lib.common.items;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import net.minecraft.world.item.Item;
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


    public @NotNull String toString() {
        return "Rune{" + this.manatype + "}";
    }
}
