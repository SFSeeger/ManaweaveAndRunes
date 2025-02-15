package io.github.sfseeger.manaweave_and_runes.datagen.client;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.SpellNodeInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ManaweaveAndRunesItemModelProvider extends ItemModelProvider {
    public ManaweaveAndRunesItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ManaweaveAndRunes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ManaweaveAndRunesItemInit.MANA_DEBUG_STICK_ITEM.get());
        basicItem(ManaweaveAndRunesItemInit.MANA_CONNECTOR.get());
        basicItem(ManaweaveAndRunesItemInit.TANZANITE.get());

        handheldItem(ManaweaveAndRunesItemInit.DIAMOND_CHISEL.get());

        basicItem(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.get());
        basicItem(ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM.get());
        basicItem(ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM.get());

        handheldItem(ManaweaveAndRunesItemInit.MANA_WEAVERS_WAND_ITEM.get());

        handheldItem(ManaweaveAndRunesItemInit.SOUL_CONTAINER_RUNE_ITEM.get())
                .texture("layer0", "item/soul_container_rune")
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "contains_soul"), 1.0F)
                .model(this.getBuilder("item/soul_container_rune/filled")
                               .parent(this.getExistingFile(mcLoc("item/handheld")))
                               .texture("layer0", "item/soul_container_rune_filled"))
                .end();

        handheldItem(ManaweaveAndRunesItemInit.POSITION_RUNE_ITEM.get())
                .texture("layer0", "item/position_rune")
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "has_position"), 1.0F)
                .model(this.getBuilder("item/position_rune_active")
                               .parent(this.getExistingFile(mcLoc("item/generated")))
                               .texture("layer0", "item/position_rune_active"))
                .end();

        SpellNodeInit.SPELL_NODES.getEntries().forEach(spellNode -> {
            ResourceLocation spellNodeId = spellNode.getId();
            if(existingFileHelper.exists(spellNodeId.withPrefix("item/"), TEXTURE)) {
                basicItem(spellNodeId).texture("layer0", "item/" + spellNodeId.getPath());
            } else {
                this.getBuilder(spellNodeId.toString())
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", "item/spell_holder_default_rune");
            }
        });
    }
}
