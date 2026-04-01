package io.github.sfseeger.lib.common.rituals.ritual_data.builtin;

import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualData;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataType;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes;
import net.minecraft.nbt.CompoundTag;

public record BooleanRitualData(boolean value) implements IRitualData {

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Value", value);
        return tag;
    }

    @Override
    public RitualDataType<BooleanRitualData> getType() {
        return RitualDataTypes.BOOLEAN_TYPE;
    }
}
