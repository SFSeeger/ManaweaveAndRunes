package io.github.sfseeger.lib.common.rituals.ritual_data;

import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class RitualDataType<T extends IRitualData> {
    private final DataSerializer<T> serializer;

    public RitualDataType(DataSerializer<T> serializer) {
        this.serializer = serializer;
    }

    public T deserialize(CompoundTag tag) {
        return serializer.deserialize(tag);
    }

    public CompoundTag serialize(T data) {
        return serializer.serialize(data);
    }

    public ResourceLocation getRegistryName() {
        return ManaweaveAndRunesRegistries.RITUAL_DATA_TYPE_REGISTRY.getKey(this);
    }

    @FunctionalInterface
    public interface DataSerializer<T extends IRitualData> {
        T deserialize(CompoundTag tag);

        default CompoundTag serialize(T data) {
            return data.serializeNBT();
        }
    }
}
