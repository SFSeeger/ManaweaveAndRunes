package io.github.sfseeger.manaweave_and_runes.common.items;

import io.github.sfseeger.lib.common.items.AbstractRuneItem;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaInit;
import net.minecraft.world.item.Item;

public class FireRuneItem extends AbstractRuneItem {
    public FireRuneItem() {
        super(new Item.Properties(), ManaInit.FIRE_MANA);
    }
}
