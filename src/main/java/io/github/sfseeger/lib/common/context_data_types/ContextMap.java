package io.github.sfseeger.lib.common.context_data_types;

import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContextMap implements INBTSerializable<CompoundTag>, Iterable<Map.Entry<String, IContextDataType>> {
    private final Map<String, IContextDataType> dataMap = new HashMap<>();

    public void putData(@Nullable String key, IContextDataType data) {
        key = key == null ? "" : "/" + key;
        dataMap.put(data.getType().getRegistryName().toString() + key, data);
    }

    @SuppressWarnings("unchecked")
    public <T extends IContextDataType, X extends ContextDataType<T>> Optional<T> getData(X type) {
        return Optional.ofNullable((T) dataMap.get(type.getRegistryName().toString()));
    }

    @SuppressWarnings("unchecked")
    public <T extends IContextDataType, X extends ContextDataType<T>> Optional<T> getData(@Nullable String key, X type) {
        key = key == null ? "" : "/" + key;
        return Optional.ofNullable((T) dataMap.get(type.getRegistryName().toString() + key));
    }

    public IContextDataType remove(String key) {
        return dataMap.remove(key);
    }

    public Set<String> keySet() {
        return dataMap.keySet();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        dataMap.forEach((key, value) -> {
            tag.put(key, value.serializeNBT());
        });
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        dataMap.clear();
        tag.getAllKeys().forEach(key -> {
            String[] sorted_key = key.split("/");
            ContextDataType<?> type =
                    ManaweaveAndRunesRegistries.CONTEXT_DATA_TYPE_REGISTRY.get(ResourceLocation.parse(sorted_key[0]));
            if (type != null) {
                this.putData(sorted_key.length > 1 ? sorted_key[1] : null, type.deserialize(tag.getCompound(key)));
            }
        });
    }

    public static ContextMap fromNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ContextMap map = new ContextMap();
        tag.getAllKeys().forEach(key -> {
            String[] sorted_key = key.split("/");
            ContextDataType<?> type =
                    ManaweaveAndRunesRegistries.CONTEXT_DATA_TYPE_REGISTRY.get(ResourceLocation.parse(sorted_key[0]));
            if (type != null) {
                map.putData(sorted_key.length > 1 ? sorted_key[1] : null, type.deserialize(tag.getCompound(key)));
            }
        });
        return map;
    }

    @Override
    public Iterator<Map.Entry<String, IContextDataType>> iterator() {
        return dataMap.entrySet().iterator();
    }

    public void clear() {
        dataMap.clear();
    }
}
