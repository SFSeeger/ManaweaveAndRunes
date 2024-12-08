package io.github.sfseeger.lib.common.items;

import io.github.sfseeger.lib.mana.Mana;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AbstractRuneItem extends Item {
    protected Supplier<Mana> manatype;

    public AbstractRuneItem(Properties properties, Supplier<Mana> manatype) {
        super(properties);
        this.manatype = manatype;
    }

    public Mana getManaType() {
        return this.manatype.get();
    }

    public IItemHandler getManaHandler() {
        return null;
    }


    public @NotNull String toString() {
        return "Rune{" + this.manatype + "}";
    }
}
