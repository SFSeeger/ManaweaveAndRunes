package io.github.sfseeger.lib.common.rituals.ritual_data.builtin;

import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualData;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.PLAYER_TYPE;

public class PlayerRitualData implements IRitualData {
    private final String playerUUID;

    public PlayerRitualData(Player player) {
        this(player.getUUID().toString());
    }

    public PlayerRitualData(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UUID getPlayerUUID() {
        return UUID.fromString(playerUUID);
    }

    public String getPlayerUUIDString() {
        return playerUUID;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("PlayerUUID", playerUUID);
        return tag;
    }

    @Override
    public RitualDataType<?> getType() {
        return PLAYER_TYPE;
    }
}
