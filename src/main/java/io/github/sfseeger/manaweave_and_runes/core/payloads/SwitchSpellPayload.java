package io.github.sfseeger.manaweave_and_runes.core.payloads;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes.MODID;

public record SwitchSpellPayload(int index) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SwitchSpellPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(MODID, "spell_switch_payload"));

    public static final StreamCodec<FriendlyByteBuf, SwitchSpellPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SwitchSpellPayload::index,
            SwitchSpellPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
