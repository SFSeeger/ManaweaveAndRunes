package io.github.sfseeger.manaweave_and_runes.core.payloads;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerViewPayload(UUID playerUUID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerViewPayload> TYPE =
            new CustomPacketPayload.Type<>(ManaweaveAndRunes.asResource("player_view_payload"));

    public static final StreamCodec<FriendlyByteBuf, PlayerViewPayload> STREAM_CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC, PlayerViewPayload::playerUUID,
                    PlayerViewPayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
