package io.github.sfseeger.lib.common.rituals.ritual_data;

import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RitualContext {
    private final Map<String, IRitualData> dataMap = new HashMap<>();

    public static RitualContext deserializeNBT(CompoundTag tag) {
        RitualContext context = new RitualContext();
        tag.getAllKeys().forEach(key -> {
            RitualDataType<?> type =
                    ManaweaveAndRunesRegistries.RITUAL_DATA_TYPE_REGISTRY.get(ResourceLocation.parse(key));
            if (type != null) {
                context.putData(type.deserialize(tag.getCompound(key)));
            }
        });
        return context;
    }

    public void putData(IRitualData data) {
        dataMap.put(data.getType().getRegistryName().toString(), data);
    }

    public <T extends IRitualData> T getData(String key, Class<T> type) {
        return type.cast(dataMap.get(key));
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        dataMap.forEach((key, value) -> tag.put(key, value.serializeNBT()));
        return tag;
    }
}
