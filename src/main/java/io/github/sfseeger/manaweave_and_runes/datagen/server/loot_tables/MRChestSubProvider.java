package io.github.sfseeger.manaweave_and_runes.datagen.server.loot_tables;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRTagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class MRChestSubProvider implements LootTableSubProvider {
    public MRChestSubProvider(HolderLookup.Provider lookupProvider) {
    }

    private static ResourceKey<LootTable> registerKey(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE,
                                  ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, name));
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(registerKey("chests/entropy_treasure"), LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(UniformGenerator.between(3, 6))
                                .setBonusRolls(UniformGenerator.between(0, 2))
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                .add(LootItem.lootTableItem(Items.AMETHYST_SHARD)
                                             .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                                             .setWeight(5)
                                )
                                .add(LootItem.lootTableItem(Items.COAL)
                                             .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8)))
                                             .setWeight(2)
                                )
                                .add(LootItem.lootTableItem(MRItemInit.DIAMOND_CHISEL)
                                             .apply(SetItemDamageFunction.setDamage(
                                                     UniformGenerator.between(0.2f, 0.9f)))
                                             .setWeight(1)
                                )
                                .add(LootItem.lootTableItem(Items.AMETHYST_BLOCK)
                                             .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                                             .setWeight(3)
                                )

                )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(UniformGenerator.between(3, 6))
                                .setBonusRolls(UniformGenerator.between(0, 2))
                                .add(TagEntry.expandTag(MRTagInit.MAGICAL_LOOT_ITEMS)
                                             .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                                             .setWeight(1)
                                )
                )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(
                                                ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                                                      "spell_effect.explode")))
                                             .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                             .setWeight(1)
                                )
                                .add(LootItem.lootTableItem(MRItemInit.ENTROPY_RUNE_CARVING_TEMPLATE)
                                             .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                             .setWeight(3)

                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(
                                                ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                                                      "spell_effect.break")))
                                             .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                             .setWeight(1)
                                )
                )
        );
    }
}
