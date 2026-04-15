package io.github.sfseeger.lib.common.context_data_types.builtin;

import io.github.sfseeger.lib.common.context_data_types.ContextDataType;
import io.github.sfseeger.lib.common.context_data_types.IContextDataType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.sfseeger.lib.common.context_data_types.ContextDataTypes.PLAYER_LIST_TYPE;

public record PlayerListContextDataType(Set<UUID> playerUUIDs) implements IContextDataType {
    public static PlayerListContextDataType fromPlayerList(Set<Player> players) {
        return new PlayerListContextDataType(players.stream().map(Player::getUUID).collect(Collectors.toSet()));
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
    public ContextDataType<?> getType() {
        return PLAYER_LIST_TYPE;
    }

    @Override
    public @Nullable IContextDataType merge(IContextDataType other) {
        if (other instanceof PlayerListContextDataType(Set<UUID> uuiDs)) {
            Set<UUID> mergedSet = new HashSet<>(this.playerUUIDs);
            mergedSet.addAll(uuiDs);
            return new PlayerListContextDataType(mergedSet);
        }
        return null;
    }

    @Override
    public IContextDataType clone() throws CloneNotSupportedException {
        return new PlayerListContextDataType(new HashSet<>(this.playerUUIDs));
    }
}
