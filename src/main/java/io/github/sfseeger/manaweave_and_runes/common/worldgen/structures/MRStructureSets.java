package io.github.sfseeger.manaweave_and_runes.common.worldgen.structures;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class MRStructureSets {
    public static final ResourceKey<StructureSet> ENTROPY_STRUCTURE = registerKey("entropy_structure");

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        context.register(ENTROPY_STRUCTURE,
                         new StructureSet(
                                 context.lookup(Registries.STRUCTURE).getOrThrow(MRStructures.ENTROPY_STRUCTURE),
                                 new RandomSpreadStructurePlacement(10, 6, RandomSpreadType.LINEAR, 1694767081)
                         ));
    }

    private static ResourceKey<StructureSet> registerKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET,
                                  ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, name));
    }
}
