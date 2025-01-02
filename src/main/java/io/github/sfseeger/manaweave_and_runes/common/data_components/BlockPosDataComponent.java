package io.github.sfseeger.manaweave_and_runes.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record BlockPosDataComponent(BlockPos pos) {
    public static final Codec<BlockPosDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(BlockPosDataComponent::pos)
    ).apply(instance, BlockPosDataComponent::new));

    public static final StreamCodec<ByteBuf, BlockPosDataComponent> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BlockPosDataComponent::pos, BlockPosDataComponent::new
    );

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof BlockPosDataComponent o && o.pos().equals(pos);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pos);
    }
}
