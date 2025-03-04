package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MRAdvancementProvider extends AdvancementProvider {
    public MRAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
            ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new ModAdvancementGenerator()));
    }

    private static class ModAdvancementGenerator implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer,
                ExistingFileHelper existingFileHelper) {
            AdvancementHolder modRoot = Advancement.Builder.advancement()
                    .display(new ItemStack(Items.AMETHYST_SHARD),
                             Component.translatable("advancements.manaweave_and_runes.root.title"),
                             Component.translatable("advancements.manaweave_and_runes.root.description"),
                             ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                                   "textures/gui/advancements/backgrounds/main.png"),
                             AdvancementType.TASK, true, true, false)
                    .addCriterion("has_amethyst_shard",
                                  InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "root"),
                          existingFileHelper);

            createTutorialAdvancements(modRoot, provider, consumer, existingFileHelper);
            createRitualAdvancements(modRoot, provider, consumer, existingFileHelper);
        }

        private void createTutorialAdvancements(AdvancementHolder modRoot, HolderLookup.Provider provider,
                Consumer<AdvancementHolder> consumer,
                ExistingFileHelper existingFileHelper) {
            AdvancementHolder manaweaversGuide = Advancement.Builder.advancement()
                    .parent(modRoot)
                    .display(PatchouliAPI.get().getBookStack(getAdvancementId("manaweavers_guide")),
                             Component.translatable(
                                     "advancements.manaweave_and_runes.tutorial.manaweavers_guide.title"),
                             Component.translatable(
                                     "advancements.manaweave_and_runes.tutorial.manaweavers_guide.description"),
                             null,
                             AdvancementType.TASK, true, true, false)
                    .addCriterion("has_patchouli_book", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            ResourceLocation.fromNamespaceAndPath("patchouli", "guide_book")))
                    .save(consumer, getAdvancementId("tutorial/manaweavers_guide"), existingFileHelper);

            AdvancementHolder tanzaniteFound = Advancement.Builder.advancement()
                    .parent(manaweaversGuide)
                    .display(new ItemStack(MRItemInit.TANZANITE.get()),
                             Component.translatable("advancements.manaweave_and_runes.tutorial.tanzanite_found.title"),
                             Component.translatable(
                                     "advancements.manaweave_and_runes.tutorial.tanzanite_found.description"),
                             null,
                             AdvancementType.TASK, true, true, false)
                    .addCriterion("has_tanzanite",
                                  InventoryChangeTrigger.TriggerInstance.hasItems(MRItemInit.TANZANITE.get()))
                    .save(consumer, getAdvancementId("tutorial/tanzanite_found"), existingFileHelper);

            AdvancementHolder templateFound = Advancement.Builder.advancement()
                    .parent(manaweaversGuide)
                    .display(new ItemStack(MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE.get()),
                             Component.translatable("advancements.manaweave_and_runes.tutorial.template_found.title"),
                             Component.translatable(
                                     "advancements.manaweave_and_runes.tutorial.template_found.description"),
                             null,
                             AdvancementType.TASK, true, true, false)
                    .addCriterion("has_fire_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.FIRE_RUNE_CARVING_TEMPLATE.get()))
                    .addCriterion("has_air_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.AIR_RUNE_CARVING_TEMPLATE.get()))
                    .addCriterion("has_earth_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.EARTH_RUNE_CARVING_TEMPLATE.get()))
                    .addCriterion("has_water_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.WATER_RUNE_CARVING_TEMPLATE.get()))
                    .addCriterion("has_void_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.VOID_RUNE_CARVING_TEMPLATE.get()))
                    .addCriterion("has_soul_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.SOUL_RUNE_CARVING_TEMPLATE.get()))
                    .addCriterion("has_order_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.ORDER_RUNE_CARVING_TEMPLATE.get()))
                    .addCriterion("has_entropy_template", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.ENTROPY_RUNE_CARVING_TEMPLATE.get()))
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(consumer, getAdvancementId("tutorial/template_found"), existingFileHelper);

            AdvancementHolder templateDuplicated = Advancement.Builder.advancement()
                    .parent(templateFound)
                    .display(new ItemStack(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE.get()),
                             Component.translatable(
                                     "advancements.manaweave_and_runes.tutorial.template_duplicated.title"),
                             Component.translatable(
                                     "advancements.manaweave_and_runes.tutorial.template_duplicated.description"),
                             null,
                             AdvancementType.TASK, true, true, false)
                    .addCriterion("made_fire_template", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            MRItemInit.FIRE_RUNE_CARVING_TEMPLATE.getKey()
                                    .location()))
                    .addCriterion("made_air_template", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            MRItemInit.AIR_RUNE_CARVING_TEMPLATE.getKey()
                                    .location()))
                    .addCriterion("made_earth_template", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            MRItemInit.EARTH_RUNE_CARVING_TEMPLATE.getKey()
                                    .location()))
                    .addCriterion("made_water_template", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            MRItemInit.WATER_RUNE_CARVING_TEMPLATE.getKey()
                                    .location()))
                    .addCriterion("made_void_template", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            MRItemInit.VOID_RUNE_CARVING_TEMPLATE.getKey()
                                    .location()))
                    .addCriterion("made_soul_template", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            MRItemInit.SOUL_RUNE_CARVING_TEMPLATE.getKey()
                                    .location()))
                    .addCriterion("made_order_template", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            MRItemInit.ORDER_RUNE_CARVING_TEMPLATE.getKey()
                                    .location()))
                    .addCriterion("made_entropy_template", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            MRItemInit.ENTROPY_RUNE_CARVING_TEMPLATE.getKey()
                                    .location()))
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(consumer, getAdvancementId("tutorial/template_duplicated"), existingFileHelper);
        }

        private void createRitualAdvancements(AdvancementHolder modRoot, HolderLookup.Provider provider,
                Consumer<AdvancementHolder> consumer,
                ExistingFileHelper existingFileHelper) {

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "rituals/");

            Item noviceRitualAnchor = BuiltInRegistries.ITEM.get(
                    ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "novice_ritual_anchor"));
            AdvancementHolder ritual_root = Advancement.Builder.advancement()
                    .parent(modRoot)
                    .addCriterion("pickup_novice_ritual_anchor",
                                  InventoryChangeTrigger.TriggerInstance.hasItems(noviceRitualAnchor))
                    .display(new ItemStack(noviceRitualAnchor),
                             Component.translatable("advancements.manaweave_and_runes.ritual_anchor.title"),
                             Component.translatable("advancements.manaweave_and_runes.ritual_anchor.description"),
                             null,
                             AdvancementType.TASK, true, true, false)
                    .rewards(AdvancementRewards.Builder.experience(100))
                    .save(consumer, id.withSuffix("root"), existingFileHelper);

            Item masterRitualAnchor = BuiltInRegistries.ITEM.get(
                    ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "master_ritual_anchor"));
            AdvancementHolder masterAnchor = Advancement.Builder.advancement()
                    .parent(ritual_root)
                    .addCriterion("pickup_master_ritual_anchor",
                                  InventoryChangeTrigger.TriggerInstance.hasItems(masterRitualAnchor))
                    .display(new ItemStack(masterRitualAnchor),
                             Component.translatable("advancements.manaweave_and_runes.master_anchor.title"),
                             Component.translatable("advancements.manaweave_and_runes.master_anchor.description"), null,
                             AdvancementType.TASK, true, true, false)
                    .rewards(AdvancementRewards.Builder.experience(500))
                    .save(consumer, id.withSuffix("master_ritual_anchor"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(ritual_root)
                    .addCriterion("has_ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                    .save(consumer, id.withSuffix("teleport_ritual"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(ritual_root)
                    .addCriterion("has_position_rune", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.POSITION_RUNE_ITEM.get()))
                    .save(consumer, id.withSuffix("thunder_ritual"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(ritual_root)
                    .addCriterion("has_break_spell", InventoryChangeTrigger.TriggerInstance.hasItems(
                            BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                                                             "spell_effect.break"))))
                    .save(consumer, id.withSuffix("shattering_rite_ritual"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(ritual_root)
                    .addCriterion("has_golden_apple", InventoryChangeTrigger.TriggerInstance.hasItems(
                            Items.GOLDEN_APPLE)) //TODO: Better condition
                    .save(consumer, id.withSuffix("sanctuary_ritual"), existingFileHelper);
            Advancement.Builder.advancement()
                    .parent(ritual_root)
                    .addCriterion("has_enchanted_golden_apple",
                                  InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENCHANTED_GOLDEN_APPLE,
                                                                                  MRItemInit.ASCENDED_RITUAL_ANCHOR_BLOCK_ITEM)) //TODO: Better condition
                    .save(consumer, id.withSuffix("ascended_sanctuary_ritual"), existingFileHelper);
        }

        private ResourceLocation getAdvancementId(String path) {
            return ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, path);
        }
    }
}
