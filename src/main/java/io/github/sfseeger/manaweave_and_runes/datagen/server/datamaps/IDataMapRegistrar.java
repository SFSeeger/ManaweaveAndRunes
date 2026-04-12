package io.github.sfseeger.manaweave_and_runes.datagen.server.datamaps;

import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.DataMapProvider;

public interface IDataMapRegistrar {
    void register(DataMapProvider provider);
}
