package io.github.sfseeger.manaweave_and_runes.common;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MRDamageTypes {
    public static final ResourceKey<DamageType> RITUAL_FAILURE =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                               ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "ritual_failure"));


    public static DamageSource createRitualFailure(Level level, BlockPos pos) {
        return new DamageSource(level.registryAccess()
                                        .registryOrThrow(Registries.DAMAGE_TYPE)
                                        .getHolderOrThrow(MRDamageTypes.RITUAL_FAILURE), null, null,
                                new Vec3(pos.getX(), pos.getY(), pos.getZ()));
    }
}
