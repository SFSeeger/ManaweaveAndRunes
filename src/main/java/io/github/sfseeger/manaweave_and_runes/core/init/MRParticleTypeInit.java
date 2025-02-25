package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.client.particles.mana_particle.ManaParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MRParticleTypeInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(
            BuiltInRegistries.PARTICLE_TYPE, ManaweaveAndRunes.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MANA_TRAVEL_PARTICLE =
            PARTICLE_TYPES.register("mana_travel_particle",
                                    () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MANA_CONCENTRATED =
            PARTICLE_TYPES.register("mana_concentrated",
                                    () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, ManaParticleType> MANA_PARTICLE =
            PARTICLE_TYPES.register("mana_particle",
                                    () -> new ManaParticleType(false));
}
