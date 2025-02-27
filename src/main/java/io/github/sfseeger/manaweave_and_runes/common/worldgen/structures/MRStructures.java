package io.github.sfseeger.manaweave_and_runes.common.worldgen.structures;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.worldgen.structures.objects.EntropyStructures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.neoforged.neoforge.common.Tags;

import java.util.Optional;

public class MRStructures {
    public static final ResourceKey<Structure> ENTROPY_STRUCTURE = registerKey("entropy_structure");

    public static void bootstrap(BootstrapContext<Structure> context) {
        context.register(ENTROPY_STRUCTURE,
                         new EntropyStructures(
                                 new Structure.StructureSettings.Builder(
                                         context.lookup(Registries.BIOME).getOrThrow(Tags.Biomes.IS_PLAINS))
                                         .terrainAdapation(TerrainAdjustment.BEARD_THIN)
                                         .build(),
                                 context.lookup(Registries.TEMPLATE_POOL)
                                         .getOrThrow(MRStructureTemplatePools.ENTROPY_STRUCTURE_TOP_TEMPLATE_POOL),
                                 Optional.empty(),
                                 2,
                                 ConstantHeight.of(VerticalAnchor.absolute(0)),
                                 Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                                 80,
                                 JigsawStructure.DEFAULT_DIMENSION_PADDING,
                                 LiquidSettings.APPLY_WATERLOGGING
                         ));
    }

    private static ResourceKey<Structure> registerKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE,
                                  ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, name));
    }
}
