package io.github.sfseeger.lib.common.items;

import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import net.minecraft.world.item.Item;

public class SpellRuneItem extends Item {
    private final AbstractSpellNode node;

    public SpellRuneItem(Properties properties, AbstractSpellNode node) {
        super(properties);
        this.node = node;
    }

    public AbstractSpellNode getSpellNode() {
        return node;
    }
}
