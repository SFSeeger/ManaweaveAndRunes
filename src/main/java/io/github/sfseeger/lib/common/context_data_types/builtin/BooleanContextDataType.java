package io.github.sfseeger.lib.common.context_data_types.builtin;

import io.github.sfseeger.lib.common.context_data_types.IContextDataType;
import io.github.sfseeger.lib.common.context_data_types.ContextDataType;
import io.github.sfseeger.lib.common.context_data_types.ContextDataTypes;
import net.minecraft.nbt.CompoundTag;

public record BooleanContextDataType(boolean value) implements IContextDataType {

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Value", value);
        return tag;
    }

    @Override
    public ContextDataType<BooleanContextDataType> getType() {
        return ContextDataTypes.BOOLEAN_TYPE;
    }

    @Override
    public IContextDataType clone() throws CloneNotSupportedException {
        return (IContextDataType) super.clone();
    }
}
