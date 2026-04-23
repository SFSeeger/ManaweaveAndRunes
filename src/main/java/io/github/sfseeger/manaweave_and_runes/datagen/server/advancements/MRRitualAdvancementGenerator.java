package io.github.sfseeger.manaweave_and_runes.datagen.server.advancements;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.util.Optional;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MRRitualAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    private final ResourceLocation advancementBaseId = ManaweaveAndRunes.asResource("rituals/");

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        Item noviceRitualAnchor = BuiltInRegistries.ITEM.get(
                ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "novice_ritual_anchor"));
        AdvancementHolder ritualRoot = Advancement.Builder.advancement()
                .parent(ManaweaveAndRunes.asResource("root"))
                .addCriterion("pickup_novice_ritual_anchor",
                              InventoryChangeTrigger.TriggerInstance.hasItems(noviceRitualAnchor))
                .display(new ItemStack(noviceRitualAnchor),
                         Component.translatable("advancements.manaweave_and_runes.ritual_anchor.title"),
                         Component.translatable("advancements.manaweave_and_runes.ritual_anchor.description"),
                         null,
                         AdvancementType.TASK, true, true, false)
                .rewards(AdvancementRewards.Builder.experience(100))
                .save(consumer, advancementBaseId.withSuffix("root"), existingFileHelper);

        Item masterRitualAnchor = BuiltInRegistries.ITEM.get(
                ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "master_ritual_anchor"));
        AdvancementHolder masterAnchor = Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("pickup_master_ritual_anchor",
                              InventoryChangeTrigger.TriggerInstance.hasItems(masterRitualAnchor))
                .display(new ItemStack(masterRitualAnchor),
                         Component.translatable("advancements.manaweave_and_runes.master_anchor.title"),
                         Component.translatable("advancements.manaweave_and_runes.master_anchor.description"), null,
                         AdvancementType.TASK, true, true, false)
                .rewards(AdvancementRewards.Builder.experience(500))
                .save(consumer, advancementBaseId.withSuffix("master_ritual_anchor"), existingFileHelper);

        createProgressionAdvancements(ritualRoot, provider, consumer, existingFileHelper);
    }

    private void createProgressionAdvancements(AdvancementHolder ritualRoot, HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("has_feather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.FEATHER))
                .save(consumer, advancementBaseId.withSuffix("particle_ritual"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("witness_lightning_strike",
                              LightningStrikeTrigger.TriggerInstance.lightningStrike(
                                      Optional.empty(), Optional.empty()
                              ))
                .save(consumer, advancementBaseId.withSuffix("thunder_ritual"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("witness_lightning_strike",
                              LightningStrikeTrigger.TriggerInstance.lightningStrike(
                                      Optional.empty(), Optional.empty()
                              ))
                .save(consumer, advancementBaseId.withSuffix("smite_ritual"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("has_ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer, advancementBaseId.withSuffix("teleport_ritual"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("has_break_spell", InventoryChangeTrigger.TriggerInstance.hasItems(
                        BuiltInRegistries.ITEM.get(ManaweaveAndRunes.asResource("spell_effect.break"))))
                .save(consumer, advancementBaseId.withSuffix("shattering_rite_ritual"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("has_golden_apple", ConsumeItemTrigger.TriggerInstance.usedItem(
                        Items.GOLDEN_APPLE))
                .save(consumer, advancementBaseId.withSuffix("sanctuary_ritual"), existingFileHelper);
        Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("placed_ascended_ritual_anchor",
                              ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(
                                      MRBlockInit.ASCENDED_RITUAL_ANCHOR_BLOCK.get())
                )
                .save(consumer, advancementBaseId.withSuffix("ascended_sanctuary_ritual"), existingFileHelper);
        Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("fell_from_distance",
                              DistanceTrigger.TriggerInstance.fallFromHeight(EntityPredicate.Builder.entity(),
                                                                             DistancePredicate.vertical(
                                                                                     MinMaxBounds.Doubles.atLeast(
                                                                                             10.0)),
                                                                             LocationPredicate.Builder.location())
                )
                .save(consumer, advancementBaseId.withSuffix("flight_ritual"), existingFileHelper);


        Advancement.Builder.advancement()
                .parent(ritualRoot)
                .addCriterion("used_bone_meal", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location(), ItemPredicate.Builder.item().of(Items.BONE_MEAL)
                ))
                .save(consumer, advancementBaseId.withSuffix("growth_ritual"), existingFileHelper);
    }
}
