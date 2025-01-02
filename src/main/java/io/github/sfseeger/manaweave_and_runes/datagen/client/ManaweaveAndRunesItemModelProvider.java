package io.github.sfseeger.manaweave_and_runes.datagen.client;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ManaweaveAndRunesItemModelProvider extends ItemModelProvider {
    public ManaweaveAndRunesItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ManaweaveAndRunes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ManaweaveAndRunesItemInit.MANA_DEBUG_STICK_ITEM.get());
        basicItem(ManaweaveAndRunesItemInit.CRYSTAL.get());

        handheldItem(ManaweaveAndRunesItemInit.DIAMOND_CHISEL.get());

        basicItem(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.get());
        basicItem(ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM.get());
        basicItem(ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM.get());

        handheldItem(ManaweaveAndRunesItemInit.SOUL_CONTAINER_RUNE_ITEM.get())
                .texture("layer0", "item/soul_container_rune")
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "contains_soul"), 1.0F)
                .model(this.getBuilder("item/soul_container_rune/filled")
                               .parent(this.getExistingFile(mcLoc("item/handheld")))
                               .texture("layer0", "item/soul_container_rune_filled"))
                .end();
    }
}
