package io.github.sfseeger.manaweave_and_runes.datagen.server.loot;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.loot.AddItemGLM;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class MRLootModifierProvider extends GlobalLootModifierProvider {
    public MRLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ManaweaveAndRunes.MODID);
    }

    @Override
    protected void start() {
        add("add_fire_carving_template_to_ruined_portal", new AddItemGLM(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.parse("chests/ruined_portal")).build(),
                LootItemRandomChanceCondition.randomChance(0.35f).build()
        }, new ItemStack(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE.get())));

        add("add_air_carving_template_to_trial_common", new AddItemGLM(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.parse("chests/trial_chambers/reward_common")).build(),
                LootItemRandomChanceCondition.randomChance(0.15f).build()
        }, new ItemStack(MRItemInit.AIR_RUNE_CARVING_TEMPLATE.get())));

        add("add_rune_block_carving_template_to_dessert_pyramid", new AddItemGLM(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/desert_pyramid")).build(),
                LootItemRandomChanceCondition.randomChance(0.9f).build()
        }, new ItemStack(MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE.get(), 4)));


    }
}
