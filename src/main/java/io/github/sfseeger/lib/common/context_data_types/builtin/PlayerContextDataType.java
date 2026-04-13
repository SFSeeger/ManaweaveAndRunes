package io.github.sfseeger.lib.common.context_data_types.builtin;

import io.github.sfseeger.lib.common.context_data_types.IContextDataType;
import io.github.sfseeger.lib.common.context_data_types.ContextDataType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static io.github.sfseeger.lib.common.context_data_types.ContextDataTypes.PLAYER_TYPE;

public class PlayerContextDataType implements IContextDataType {
    private final String playerUUID;

    public PlayerContextDataType(Player player) {
        this(player.getUUID().toString());
    }

    public PlayerContextDataType(String playerUUID) {
        this.playerUUID = playerUUID;
    }
    public PlayerContextDataType(UUID playerUUID) {
        this.playerUUID = playerUUID.toString();
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
    public ContextDataType<?> getType() {
        return PLAYER_TYPE;
    }

    @Override
    public IContextDataType clone() throws CloneNotSupportedException {
        return (PlayerContextDataType) super.clone();
    }
}
