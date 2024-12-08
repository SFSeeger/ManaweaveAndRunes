package io.github.sfseeger.manaweave_and_runes.common.api.mana;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ManaRegistry {
    public static final ResourceKey<Registry<Mana>> MANA_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana"));
    public static final Registry<Mana> MANA_REGISTRY = new RegistryBuilder<>(MANA_REGISTRY_KEY)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "empty_mana")) //TODO: Change this to the actual default key
            .maxId(256)
            .create();
}
