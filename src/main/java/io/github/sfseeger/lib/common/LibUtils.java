package io.github.sfseeger.lib.common;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

public class LibUtils {
    public static <T> Tag encode(Codec<T> codec, T value, HolderLookup.Provider levelRegistry) {
        return (Tag) codec.encodeStart(levelRegistry.createSerializationContext(NbtOps.INSTANCE), value).getOrThrow();
    }

    public static <T> T decode(Codec<T> codec, Tag tag, HolderLookup.Provider levelRegistry) {
        return codec.parse(levelRegistry.createSerializationContext(NbtOps.INSTANCE), tag).getOrThrow();
    }
}
