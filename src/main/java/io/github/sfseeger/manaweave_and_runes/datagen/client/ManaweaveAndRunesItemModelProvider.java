package io.github.sfseeger.manaweave_and_runes.datagen.client;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.SpellNodeInit;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class ManaweaveAndRunesItemModelProvider extends ItemModelProvider {
    public ManaweaveAndRunesItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ManaweaveAndRunes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MRItemInit.MANA_DEBUG_STICK_ITEM.get());
        basicItem(MRItemInit.MANA_CONNECTOR.get());
        basicItem(MRItemInit.TANZANITE.get());

        handheldItem(MRItemInit.DIAMOND_CHISEL.get());

        basicItem(MRItemInit.AMETHYST_BASE_RUNE.get());
        basicItem(MRItemInit.AMETHYST_FIRE_RUNE_ITEM.get());
        basicItem(MRItemInit.AMETHYST_AIR_RUNE_ITEM.get());

        basicItem(MRItemInit.SPELL_HOLDER_ITEM.get());

        handheldItem(MRItemInit.SOUL_CONTAINER_RUNE_ITEM.get())
                .texture("layer0", "item/soul_container_rune")
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "contains_soul"), 1.0F)
                .model(this.getBuilder("item/soul_container_rune/filled")
                               .parent(this.getExistingFile(mcLoc("item/handheld")))
                               .texture("layer0", "item/soul_container_rune_filled"))
                .end();

        handheldItem(MRItemInit.POSITION_RUNE_ITEM.get())
                .texture("layer0", "item/position_rune")
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "has_position"), 1.0F)
                .model(this.getBuilder("item/position_rune_active")
                               .parent(this.getExistingFile(mcLoc("item/generated")))
                               .texture("layer0", "item/position_rune_active"))
                .end();

        basicItem(MRItemInit.SPELL_PART.get());
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

    private ItemModelBuilder blockWithExistingModel(Block block, ResourceLocation existingModel) {
        return this.withExistingParent(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).toString(),
                                       existingModel);
    }
}
