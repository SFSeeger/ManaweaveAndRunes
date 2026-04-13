package io.github.sfseeger.lib.common.context_data_types;

import net.minecraft.nbt.CompoundTag;

public interface IContextDataType extends Cloneable {
    CompoundTag serializeNBT();

    ContextDataType<?> getType();

    IContextDataType clone() throws CloneNotSupportedException;
}
