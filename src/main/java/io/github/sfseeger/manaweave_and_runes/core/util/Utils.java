package io.github.sfseeger.manaweave_and_runes.core.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

public class Utils {
    public static <T> Tag encode(Codec<T> codec, T value, HolderLookup.Provider levelRegistry) {
        return (Tag) codec.encodeStart(levelRegistry.createSerializationContext(NbtOps.INSTANCE), value).getOrThrow();
    }
}
