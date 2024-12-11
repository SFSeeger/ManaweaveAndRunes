package io.github.sfseeger.lib.common.mana;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import static io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesDataComponentsInit.MANA_DATA_COMPONENT;

public class ManaHelper {
    public static void setItemMana(ModifyDefaultComponentsEvent event, Item item, Mana manaType, int manaValue) {
        event.modify(item, builder ->
                builder.set(MANA_DATA_COMPONENT.value(), new ManaDateComponent(
                        manaType, manaValue
                )));

    }
}
