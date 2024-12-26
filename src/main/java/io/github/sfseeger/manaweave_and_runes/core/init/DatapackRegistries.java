package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.util.MultiblockValidator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = ManaweaveAndRunes.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DatapackRegistries {
    public static final ResourceKey<Registry<MultiblockValidator>>
            MULTIBLOCK_VALIDATOR_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(
            ManaweaveAndRunes.MODID, "multiblocks"));

    public static final ResourceKey<MultiblockValidator> NOVICE_MULTIBLOCK = ResourceKey.create(
            MULTIBLOCK_VALIDATOR_REGISTRY_KEY, ResourceLocation.fromNamespaceAndPath(
                    ManaweaveAndRunes.MODID, "novice_mana_concentrator"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                MULTIBLOCK_VALIDATOR_REGISTRY_KEY,
                MultiblockValidator.CODEC,
                MultiblockValidator.CODEC
        );
    }
}
