package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MRAdvancementProvider extends AdvancementProvider {
    public MRAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
            ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new RitualUnlockAdvancementGenerator()));
    }

    private static class RitualUnlockAdvancementGenerator implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer,
                ExistingFileHelper existingFileHelper) {
            createRitualAdvancements(provider, consumer, existingFileHelper);


        }

        private void createRitualAdvancements(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer,
                ExistingFileHelper existingFileHelper) {

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "rituals/");

            Item noviceRitualAnchor = BuiltInRegistries.ITEM.get(
                    ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "novice_ritual_anchor"));
            AdvancementHolder root = Advancement.Builder.advancement()
                    .addCriterion("pickup_novice_ritual_anchor",
                                  InventoryChangeTrigger.TriggerInstance.hasItems(noviceRitualAnchor))
                    .display(new ItemStack(noviceRitualAnchor),
                             Component.translatable("advancements.manaweave_and_runes.ritual_anchor.title"),
                             Component.translatable("advancements.manaweave_and_runes.ritual_anchor.description"),
                             ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                                   "textures/blocks/rune_block_inactive"),
                             //TODO: Add texture
                             AdvancementType.TASK, true, true, false)
                    .rewards(AdvancementRewards.Builder.experience(100))
                    .save(consumer, id.withSuffix("root"), existingFileHelper);

            Item masterRitualAnchor = BuiltInRegistries.ITEM.get(
                    ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "master_ritual_anchor"));
            AdvancementHolder masterAnchor = Advancement.Builder.advancement()
                    .parent(root)
                    .addCriterion("pickup_master_ritual_anchor",
                                  InventoryChangeTrigger.TriggerInstance.hasItems(masterRitualAnchor))
                    .display(new ItemStack(masterRitualAnchor),
                             Component.translatable("advancements.manaweave_and_runes.master_anchor.title"),
                             Component.translatable("advancements.manaweave_and_runes.master_anchor.description"), null,
                             AdvancementType.TASK, true, true, false)
                    .rewards(AdvancementRewards.Builder.experience(500))
                    .save(consumer, id.withSuffix("master_ritual_anchor"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .addCriterion("has_ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                    .save(consumer, id.withSuffix("teleport_ritual"), existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .addCriterion("has_position_rune", InventoryChangeTrigger.TriggerInstance.hasItems(
                            MRItemInit.POSITION_RUNE_ITEM.get()))
                    .save(consumer, id.withSuffix("thunder_ritual"), existingFileHelper);
        }
    }
}
