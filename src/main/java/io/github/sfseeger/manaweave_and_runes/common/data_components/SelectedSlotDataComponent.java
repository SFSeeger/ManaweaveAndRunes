package io.github.sfseeger.manaweave_and_runes.common.data_components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SelectedSlotDataComponent(int selectedSlot) {
    public static final Codec<SelectedSlotDataComponent> CODEC = Codec.INT.fieldOf("slot")
            .xmap(SelectedSlotDataComponent::new, SelectedSlotDataComponent::selectedSlot)
            .codec();
    public static final StreamCodec<ByteBuf, SelectedSlotDataComponent> STREAM_CODEC =
            ByteBufCodecs.INT.map(SelectedSlotDataComponent::new, SelectedSlotDataComponent::selectedSlot);

    public SelectedSlotDataComponent {
        if (selectedSlot < 0) {
            throw new IllegalArgumentException("Selected slot must be greater than or equal to 0");
        }
    }
}
