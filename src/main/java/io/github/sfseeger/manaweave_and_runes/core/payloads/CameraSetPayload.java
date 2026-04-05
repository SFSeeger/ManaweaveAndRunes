package io.github.sfseeger.manaweave_and_runes.core.payloads;

import com.mojang.serialization.Codec;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record CameraSetPayload(int entityUUID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CameraSetPayload> TYPE =
            new CustomPacketPayload.Type<>(ManaweaveAndRunes.asResource("player_view_payload"));

    public static final StreamCodec<FriendlyByteBuf, CameraSetPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, CameraSetPayload::entityUUID,
                    CameraSetPayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
