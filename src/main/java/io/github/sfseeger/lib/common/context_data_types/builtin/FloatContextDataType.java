package io.github.sfseeger.lib.common.context_data_types.builtin;

import io.github.sfseeger.lib.common.context_data_types.ContextDataType;
import io.github.sfseeger.lib.common.context_data_types.ContextDataTypes;
import io.github.sfseeger.lib.common.context_data_types.IContextDataType;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record FloatContextDataType(float value) implements IContextDataType {
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Value", value);
        return tag;
    }

    @Override
    public ContextDataType<?> getType() {
        return ContextDataTypes.FLOAT_TYPE;
    }

    @Override
    public @Nullable IContextDataType merge(IContextDataType other) {
        if (other instanceof FloatContextDataType(float value1)) {
            return new FloatContextDataType(this.value + value1);
        }
        return null;
    }

    @Override
    public IContextDataType clone() throws CloneNotSupportedException {
        return (IContextDataType) super.clone();
    }
}
