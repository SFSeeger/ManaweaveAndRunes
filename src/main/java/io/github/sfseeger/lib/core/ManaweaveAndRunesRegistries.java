package io.github.sfseeger.lib.core;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualInput;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataType;
import io.github.sfseeger.lib.common.spells.AbstractSpellNode;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ManaweaveAndRunesRegistries {
    public static final ResourceKey<Registry<Mana>> MANA_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana"));
    public static final Registry<Mana> MANA_REGISTRY = new RegistryBuilder<>(MANA_REGISTRY_KEY)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "empty_mana")) //TODO: Change this to the actual default key
            .maxId(256)
            .create();

    public static ResourceKey<Registry<Ritual>> RITUAL_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "ritual"));
    public static Registry<Ritual> RITUAL_REGISTRY = new RegistryBuilder<>(RITUAL_REGISTRY_KEY)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                              "default_ritual")) //TODO: Change this to the actual default key
            .maxId(256)
            .create();

    public static final ResourceKey<Registry<RitualDataType<?>>> RITUAL_DATA_TYPE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(
                    ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "ritual_data_type"));
    public static final Registry<RitualDataType<?>> RITUAL_DATA_TYPE_REGISTRY =
            new RegistryBuilder<>(RITUAL_DATA_TYPE_REGISTRY_KEY)
                    .sync(true)
                    .defaultKey(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                                      "default")) //TODO: Change this to the actual default key
                    .maxId(64)
                    .create();

    public static final ResourceKey<Registry<AbstractSpellNode>> SPELL_NODE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "spell_node"));
    public static final Registry<AbstractSpellNode> SPELL_NODE_REGISTRY = new RegistryBuilder<>(SPELL_NODE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "default_spell_node"))
            .maxId(256)
            .create();

    // Datapack registries
    public static final ResourceKey<Registry<MultiblockValidator>>
            MULTIBLOCK_VALIDATOR_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(
            ManaweaveAndRunes.MODID, "multiblocks"));
    public static final ResourceKey<Registry<RitualInput>> RITUAL_INPUT_REGISTRY_KEY =
            ResourceKey.createRegistryKey(
                    ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "ritual_inputs"));
}
