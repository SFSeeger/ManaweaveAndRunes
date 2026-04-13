package io.github.sfseeger.lib.common.context_data_types;

import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class ContextDataType<T extends IContextDataType> {
    private final DataSerializer<T> serializer;

    public ContextDataType(DataSerializer<T> serializer) {
        this.serializer = serializer;
    }

    public T deserialize(CompoundTag tag) {
        return serializer.deserialize(tag);
    }

    public CompoundTag serialize(T data) {
        return serializer.serialize(data);
    }

    public ResourceLocation getRegistryName() {
        return ManaweaveAndRunesRegistries.CONTEXT_DATA_TYPE_REGISTRY.getKey(this);
    }

    @FunctionalInterface
    public interface DataSerializer<T extends IContextDataType> {
        T deserialize(CompoundTag tag);

        default CompoundTag serialize(T data) {
            return data.serializeNBT();
        }
    }
}
