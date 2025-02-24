package io.github.sfseeger.lib.common.rituals.ritual_data.builtin;

import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualData;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.PLAYER_LIST_TYPE;

public record PlayerListRitualData(Set<UUID> playerUUIDs) implements IRitualData {
    public static PlayerListRitualData fromPlayerList(Set<Player> players) {
        return new PlayerListRitualData(players.stream().map(Player::getUUID).collect(Collectors.toSet()));
    }

    public void addPlayer(Player player) {
        playerUUIDs.add(player.getUUID());
    }

    public boolean removePlayer(Player player) {
        return playerUUIDs.remove(player.getUUID());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        playerUUIDs.forEach(player -> list.add(new IntArrayTag(UUIDUtil.uuidToIntArray(player))));
        tag.put("Players", list);
        return tag;
    }

    @Override
    public RitualDataType<?> getType() {
        return PLAYER_LIST_TYPE;
    }
}
