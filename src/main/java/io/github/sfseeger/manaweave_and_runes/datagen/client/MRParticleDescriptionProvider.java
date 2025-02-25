package io.github.sfseeger.manaweave_and_runes.datagen.client;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRParticleTypeInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class MRParticleDescriptionProvider extends ParticleDescriptionProvider {
    public MRParticleDescriptionProvider(PackOutput output,
            ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        sprite(MRParticleTypeInit.MANA_TRAVEL_PARTICLE.get(),
               ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana_travel"));

        spriteSet(MRParticleTypeInit.MANA_CONCENTRATED.get(),
                  ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana_concentrated"), 14, false);

        sprite(MRParticleTypeInit.MANA_PARTICLE.get(),
               ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "mana"));
    }
}
