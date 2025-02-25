package io.github.sfseeger.manaweave_and_runes.datagen.server.datapack;

import io.github.sfseeger.manaweave_and_runes.common.MRDamageTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public class MRDamageTypeProvider {
    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(MRDamageTypes.RITUAL_FAILURE,
                         new DamageType(MRDamageTypes.RITUAL_FAILURE.location().toLanguageKey("death"),
                                        DamageScaling.ALWAYS,
                                        0.1f,
                                        DamageEffects.HURT,
                                        DeathMessageType.DEFAULT));
    }
}
