package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.items.AbstractRuneItem;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.SingleManaHandler;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaInit;
import net.minecraft.world.item.Item;

public class FireRuneItem extends AbstractRuneItem {
    protected final SingleManaHandler manaHandler;

    public FireRuneItem() {
        super(new Item.Properties(), ManaInit.FIRE_MANA);
        this.manaHandler = new SingleManaHandler(1000, 500, 500, ManaInit.FIRE_MANA);
    }

    @Override
    public IManaHandler getManaHandler() {
        return manaHandler;
    }
}
