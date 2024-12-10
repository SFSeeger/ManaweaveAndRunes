package io.github.sfseeger.manaweave_and_runes.datagen.client;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ManaweaveAndRunesItemModelProvider extends ItemModelProvider {
    public ManaweaveAndRunesItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ManaweaveAndRunes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ManaweaveAndRunesItemInit.MANA_DEBUG_STICK_ITEM.get());
        basicItem(ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM.get());
        basicItem(ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM.get());
    }
}
