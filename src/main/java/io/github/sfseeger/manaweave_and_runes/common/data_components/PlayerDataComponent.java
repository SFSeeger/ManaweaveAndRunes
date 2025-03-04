package io.github.sfseeger.manaweave_and_runes.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.Optional;

public record PlayerDataComponent(String playerUUID, String lastPlayerName) {
    public static final Codec<PlayerDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("playerUUID").forGetter(PlayerDataComponent::playerUUID),
            Codec.STRING.optionalFieldOf("lastPlayerName", "").forGetter(PlayerDataComponent::lastPlayerName)
    ).apply(instance, PlayerDataComponent::new));


    public static final StreamCodec<ByteBuf, PlayerDataComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PlayerDataComponent::playerUUID,
            ByteBufCodecs.STRING_UTF8, PlayerDataComponent::lastPlayerName,
            PlayerDataComponent::new
    );

    public PlayerDataComponent(Player player) {
        this(player.getStringUUID(),
             Optional.ofNullable(player.getDisplayName()).orElse(Component.empty()).getString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerDataComponent that)) return false;
        return Objects.equals(playerUUID, that.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerUUID);
    }
}
