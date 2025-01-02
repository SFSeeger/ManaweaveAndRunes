package io.github.sfseeger.lib.common;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;

import java.util.List;

public class ManaweaveAndRunesCodecs {
    public static final Codec<List<BlockPos>> BLOCK_POS_LIST_CODEC = BlockPos.CODEC.listOf();
}
