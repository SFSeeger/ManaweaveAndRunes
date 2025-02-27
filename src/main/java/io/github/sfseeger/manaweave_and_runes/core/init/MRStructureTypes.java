package io.github.sfseeger.manaweave_and_runes.core.init;

import com.mojang.serialization.MapCodec;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.worldgen.structures.objects.EntropyStructures;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MRStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, ManaweaveAndRunes.MODID);

    public static final DeferredHolder<StructureType<?>, StructureType<EntropyStructures>> ENTROPY_STRUCTURES =
            STRUCTURES.register("entropy_structures", () -> explicitStructureTypeTyping(
                    EntropyStructures.CODEC));

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(MapCodec<T> structureCodec) {
        return () -> structureCodec;
    }
}
