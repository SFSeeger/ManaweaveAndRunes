package io.github.sfseeger.manaweave_and_runes.datagen.client;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.SpellNodeInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MRItemModelProvider extends ItemModelProvider {
    public MRItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ManaweaveAndRunes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MRItemInit.MANA_DEBUG_STICK_ITEM.get());
        handheldItem(MRItemInit.MANA_CONNECTOR.get());
        basicItem(MRItemInit.TANZANITE.get());

        handheldItem(MRItemInit.DIAMOND_CHISEL.get());
        basicItem(MRItemInit.SPELL_HOLDER_ITEM.get());

        basicItem(MRItemInit.AMETHYST_BASE_RUNE.get());
        basicItem(MRItemInit.AMETHYST_FIRE_RUNE_ITEM.get());
        basicItem(MRItemInit.AMETHYST_AIR_RUNE_ITEM.get());
        basicItem(MRItemInit.AMETHYST_WATER_RUNE_ITEM.get());
        basicItem(MRItemInit.AMETHYST_EARTH_RUNE_ITEM.get());
        basicItem(MRItemInit.AMETHYST_SOUL_RUNE_ITEM.get());
        basicItem(MRItemInit.AMETHYST_VOID_RUNE_ITEM.get());
        basicItem(MRItemInit.AMETHYST_ORDER_RUNE_ITEM.get());
        basicItem(MRItemInit.AMETHYST_ENTROPY_RUNE_ITEM.get());

        basicItem(MRItemInit.AIR_RUNE_CARVING_TEMPLATE.get());
        basicItem(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE.get());
        basicItem(MRItemInit.WATER_RUNE_CARVING_TEMPLATE.get());
        basicItem(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE.get());
        basicItem(MRItemInit.SOUL_RUNE_CARVING_TEMPLATE.get());
        basicItem(MRItemInit.VOID_RUNE_CARVING_TEMPLATE.get());
        basicItem(MRItemInit.ORDER_RUNE_CARVING_TEMPLATE.get());
        basicItem(MRItemInit.ENTROPY_RUNE_CARVING_TEMPLATE.get());
        basicItem(MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE.get());

        handheldItem(MRItemInit.MANA_WEAVER_WAND_ITEM.get());

        handheldItem(MRItemInit.POSITION_RUNE_ITEM.get())
                .texture("layer0", "item/position_rune")
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "has_position"), 1.0F)
                .model(this.getBuilder("item/position_rune_active")
                               .parent(this.getExistingFile(mcLoc("item/generated")))
                               .texture("layer0", "item/position_rune_active"))
                .end();
        handheldItem(MRItemInit.SOUL_CONTAINER_RUNE_ITEM.get())
                .texture("layer0", "item/soul_container_rune")
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "contains_soul"), 1.0F)
                .model(this.getBuilder("item/soul_container_rune/filled")
                               .parent(this.getExistingFile(mcLoc("item/handheld")))
                               .texture("layer0", "item/soul_container_rune_filled"))
                .end();


        basicItem(MRItemInit.SPELL_PART.get());
        SpellNodeInit.SPELL_NODES.getEntries().forEach(spellNode -> {
            ResourceLocation spellNodeId = spellNode.getId();
            if (existingFileHelper.exists(spellNodeId.withPrefix("item/"), TEXTURE)) {
                basicItem(spellNodeId).texture("layer0", "item/" + spellNodeId.getPath());
            } else {
                this.getBuilder(spellNodeId.toString())
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", "item/spell_holder_default_rune");
            }
        });
    }
}
