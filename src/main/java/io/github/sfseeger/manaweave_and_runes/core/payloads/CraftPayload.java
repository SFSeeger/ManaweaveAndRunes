package io.github.sfseeger.manaweave_and_runes.core.payloads;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes.MODID;

public record CraftPayload(long blockPos, int actionId, String customName) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CraftPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(MODID, "craft_payload"));

    public static final StreamCodec<FriendlyByteBuf, CraftPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, CraftPayload::blockPos,
            ByteBufCodecs.INT, CraftPayload::actionId,
            ByteBufCodecs.STRING_UTF8, CraftPayload::customName,
            CraftPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
