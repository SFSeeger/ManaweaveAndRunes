package io.github.sfseeger.lib.common.rituals.ritual_data;

import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RitualContext {
    private final Map<String, IRitualData> dataMap = new HashMap<>();

    public static RitualContext deserializeNBT(CompoundTag tag) {
        RitualContext context = new RitualContext();
        tag.getAllKeys().forEach(key -> {
            String[] sorted_key = key.split("/");
            RitualDataType<?> type =
                    ManaweaveAndRunesRegistries.RITUAL_DATA_TYPE_REGISTRY.get(ResourceLocation.parse(sorted_key[0]));
            if (type != null) {
                context.putData(sorted_key.length > 1 ? sorted_key[1] : null, type.deserialize(tag.getCompound(key)));
            }
        });
        return context;
    }

    public void putData(@Nullable String key, IRitualData data) {
        key = key == null ? "" : "/" + key;
        dataMap.put(data.getType().getRegistryName().toString() + key, data);
    }

    @SuppressWarnings("unchecked")
    public <T extends IRitualData, X extends RitualDataType<T>> T getData(X type) {
        return (T) dataMap.get(type.getRegistryName().toString());
    }

    @SuppressWarnings("unchecked")
    public <T extends IRitualData, X extends RitualDataType<T>> T getData(@Nullable String key, X type) {
        key = key == null ? "" : "/" + key;
        return (T) dataMap.get(type.getRegistryName().toString() + key);
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        dataMap.forEach((key, value) -> {
            tag.put(key, value.serializeNBT());
        });
        return tag;
    }
}
