package io.github.sfseeger.lib.common.rituals.ritual_data;

import net.minecraft.nbt.CompoundTag;

public interface IRitualData {
    CompoundTag serializeNBT();

    RitualDataType<?> getType();
}
