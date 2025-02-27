package io.github.sfseeger.manaweave_and_runes.common.worldgen.structures;

import com.mojang.datafixers.util.Pair;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.List;

public class MRStructureTemplatePools {
    public static final ResourceKey<StructureTemplatePool>
            FALLBACK = ResourceKey.create(Registries.TEMPLATE_POOL,
                                          ResourceLocation.withDefaultNamespace("empty"));

    public static final ResourceKey<StructureTemplatePool>
            ENTROPY_STRUCTURE_TOP_TEMPLATE_POOL = registerTemplateKey("entropy_structure/top");
    public static final ResourceKey<StructureTemplatePool>
            ENTROPY_STRUCTURE_BOTTOM_TEMPLATE_POOL = registerTemplateKey("entropy_structure/bottom");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        context.register(ENTROPY_STRUCTURE_TOP_TEMPLATE_POOL,
                         new StructureTemplatePool(
                                 context.lookup(Registries.TEMPLATE_POOL).getOrThrow(FALLBACK),
                                 List.of(
                                         Pair.of(
                                                 StructurePoolElement.single(
                                                                 ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                                                                       "entropy_structure_top_1")
                                                                         .toString())
                                                         .apply(StructureTemplatePool.Projection.RIGID),
                                                 1)
                                 )
                         )
        );
        context.register(ENTROPY_STRUCTURE_BOTTOM_TEMPLATE_POOL,
                         new StructureTemplatePool(
                                 context.lookup(Registries.TEMPLATE_POOL).getOrThrow(FALLBACK),
                                 List.of(
                                         Pair.of(
                                                 StructurePoolElement.single(
                                                                 ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                                                                       "entropy_structure_bottom_1")
                                                                         .toString())
                                                         .apply(StructureTemplatePool.Projection.RIGID),
                                                 1)
                                 )
                         )
        );
    }

    private static ResourceKey<StructureTemplatePool> registerTemplateKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL,
                                  ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, name));
    }
}
