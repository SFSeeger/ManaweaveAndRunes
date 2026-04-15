package io.github.sfseeger.lib.common.context_data_types;

import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public interface IContextDataType extends Cloneable {
    CompoundTag serializeNBT();

    ContextDataType<?> getType();

    /*
     * Merges this context data type with another one of the same type. Returns a new instance of the merged data type, or null if they cannot be merged.
     */
    @Nullable IContextDataType merge(IContextDataType other);

    default @Nullable IContextDataType merge(IContextDataType other, BiFunction<IContextDataType, IContextDataType, IContextDataType> merger) {;
        return merger.apply(this, other);
    }

    IContextDataType clone() throws CloneNotSupportedException;
}
