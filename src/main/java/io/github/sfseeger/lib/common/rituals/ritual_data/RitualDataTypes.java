package io.github.sfseeger.lib.common.rituals.ritual_data;

import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerListRitualData;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PositionRitualData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Collectors;

public class RitualDataTypes {
    public static final RitualDataType<?> DEFAULT_TYPE = new RitualDataType<>(tag -> null);

    public static final RitualDataType<PositionRitualData> POSITION_TYPE =
            new RitualDataType<>(tag -> new PositionRitualData(
                    BlockPos.of(tag.getLong("Position"))));

    public static final RitualDataType<PlayerRitualData> PLAYER_TYPE =
            new RitualDataType<>(tag -> new PlayerRitualData(
                    tag.getString("PlayerUUID")));

    public static final RitualDataType<PlayerListRitualData> PLAYER_LIST_TYPE =
            new RitualDataType<>(tag -> new PlayerListRitualData(
                    tag.getList("Players", Tag.TAG_INT_ARRAY)
                            .stream()
                            .map(array -> UUIDUtil.uuidFromIntArray(((IntArrayTag) array).getAsIntArray()))
                            .collect(
                                    Collectors.toSet())));


    public static void register(DeferredRegister<RitualDataType<?>> registry) {
        registry.register("default", () -> DEFAULT_TYPE);
        registry.register("position", () -> POSITION_TYPE);
        registry.register("player", () -> PLAYER_TYPE);
        registry.register("player_list", () -> PLAYER_LIST_TYPE);
    }
}
