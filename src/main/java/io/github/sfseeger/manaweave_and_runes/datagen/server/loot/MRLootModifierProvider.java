package io.github.sfseeger.manaweave_and_runes.datagen.server.loot;

import io.github.sfseeger.lib.common.items.RuneCarvingTemplate;
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
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.concurrent.CompletableFuture;

public class MRLootModifierProvider extends GlobalLootModifierProvider {
    public MRLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ManaweaveAndRunes.MODID);
    }

    @Override
    protected void start() {
        addTemplate(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE, 0.35f, ResourceLocation.parse("chests/ruined_portal"));
        addTemplate(MRItemInit.FIRE_RUNE_CARVING_TEMPLATE, 0.5f, ResourceLocation.parse("chests/nether_bridge"));

        addTemplate(MRItemInit.AIR_RUNE_CARVING_TEMPLATE, 0.15f, ResourceLocation.parse("chests/jungle_temple"));
        addTemplate(MRItemInit.AIR_RUNE_CARVING_TEMPLATE, 0.7f,
                    ResourceLocation.parse("chests/trial_chambers/corridor"));
        addTemplate(MRItemInit.AIR_RUNE_CARVING_TEMPLATE, 0.4f,
                    ResourceLocation.parse("chests/trial_chambers/entrance"));
        addTemplate(MRItemInit.AIR_RUNE_CARVING_TEMPLATE, 0.7f,
                    ResourceLocation.parse("chests/trial_chambers/corridor"));
        addTemplate(MRItemInit.AIR_RUNE_CARVING_TEMPLATE, 0.6f, ResourceLocation.parse("chests/trial_chambers/reward"));

        addTemplate(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE, 0.2f, ResourceLocation.parse("chests/desert_pyramid"));
        addTemplate(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE, 0.6f, ResourceLocation.parse("chests/jungle_temple"));
        addTemplate(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE, 0.35f, ResourceLocation.parse("chests/buried_treasure"));
        addTemplate(MRItemInit.EARTH_RUNE_CARVING_TEMPLATE, 0.6f, ResourceLocation.parse("chests/woodland_mansion"));


        addTemplate(MRItemInit.WATER_RUNE_CARVING_TEMPLATE, 0.7f, ResourceLocation.parse("chests/buried_treasure"));
        addTemplate(MRItemInit.WATER_RUNE_CARVING_TEMPLATE, 0.7f, ResourceLocation.parse("chests/underwater_ruin_big"));
        addTemplate(MRItemInit.WATER_RUNE_CARVING_TEMPLATE, 0.4f,
                    ResourceLocation.parse("chests/underwater_ruin_small"));

        addTemplate(MRItemInit.VOID_RUNE_CARVING_TEMPLATE, 0.14f, ResourceLocation.parse("chests/stronghold_corridor"));
        addTemplate(MRItemInit.VOID_RUNE_CARVING_TEMPLATE, 0.6f, ResourceLocation.parse("chests/stronghold_crossing"));
        addTemplate(MRItemInit.VOID_RUNE_CARVING_TEMPLATE, 0.5f, ResourceLocation.parse("chests/ancient_city"));

        addTemplate(MRItemInit.SOUL_RUNE_CARVING_TEMPLATE, 0.8f, ResourceLocation.parse("chests/woodland_mansion"));
        addTemplate(MRItemInit.SOUL_RUNE_CARVING_TEMPLATE, 0.7f, ResourceLocation.parse("chests/ancient_city"));
        addTemplate(MRItemInit.SOUL_RUNE_CARVING_TEMPLATE, 0.5f, ResourceLocation.parse("chests/simple_dungeon"));

        addTemplate(MRItemInit.ORDER_RUNE_CARVING_TEMPLATE, 0.15f,
                    ResourceLocation.parse("chests/village/village_temple"));
        addTemplate(MRItemInit.ORDER_RUNE_CARVING_TEMPLATE, 0.7f, ResourceLocation.parse("chests/woodland_mansion"));
        addTemplate(MRItemInit.ORDER_RUNE_CARVING_TEMPLATE, 0.43f, ResourceLocation.parse("chests/bastion_treasure"));

        addTemplate(MRItemInit.ENTROPY_RUNE_CARVING_TEMPLATE, 0.5f,
                    ResourceLocation.parse("chests/stronghold_library"));


        add("add_rune_block_carving_template_to_dessert_pyramid", new AddItemGLM(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.parse("chests/desert_pyramid")).build(),
                LootItemRandomChanceCondition.randomChance(0.8f).build()
        }, new ItemStack(MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE.get(), 4)));
        add("add_rune_block_carving_template_to_jungle_temple", new AddItemGLM(new LootItemCondition[]{
                LootTableIdCondition.builder(ResourceLocation.parse("chests/jungle_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.7f).build()
        }, new ItemStack(MRItemInit.RUNE_BLOCK_CARVING_TEMPLATE.get(), 2)));

    }

    private void addTemplate(DeferredItem<RuneCarvingTemplate> template, float chance, ResourceLocation loot_table) {
        add("add_" + template.getId().getPath() + "_to_" + loot_table.getPath().replace("/", "_"),
            new AddItemGLM(new LootItemCondition[]{
                    LootTableIdCondition.builder(loot_table).build(),
                    LootItemRandomChanceCondition.randomChance(chance).build()
            }, new ItemStack(template.get())));
    }
}
