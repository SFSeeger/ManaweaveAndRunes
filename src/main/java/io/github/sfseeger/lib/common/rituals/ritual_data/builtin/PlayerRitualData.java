package io.github.sfseeger.lib.common.rituals.ritual_data.builtin;

import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualData;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataType;
import net.minecraft.nbt.CompoundTag;

import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.PLAYER_TYPE;

public class PlayerRitualData implements IRitualData {
    private final String playerUUID;

    public PlayerRitualData(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getPlayerUUID() {
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
