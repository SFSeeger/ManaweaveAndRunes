package io.github.sfseeger.lib.common.context_data_types;

import io.github.sfseeger.lib.common.context_data_types.builtin.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Collectors;

public class ContextDataTypes {
    public static final ContextDataType<?> DEFAULT_TYPE = new ContextDataType<>(tag -> null);

    public static final ContextDataType<PositionContextDataType> POSITION_TYPE =
            new ContextDataType<>(tag -> new PositionContextDataType(
                    BlockPos.of(tag.getLong("Position"))));

    public static final ContextDataType<PlayerContextDataType> PLAYER_TYPE =
            new ContextDataType<>(tag -> new PlayerContextDataType(
                    tag.getString("PlayerUUID")));

    public static final ContextDataType<PlayerListContextDataType> PLAYER_LIST_TYPE =
            new ContextDataType<>(tag -> new PlayerListContextDataType(
                    tag.getList("Players", Tag.TAG_INT_ARRAY)
                            .stream()
                            .map(array -> UUIDUtil.uuidFromIntArray(((IntArrayTag) array).getAsIntArray()))
                            .collect(
                                    Collectors.toSet())));

    public static final ContextDataType<BooleanContextDataType> BOOLEAN_TYPE =
            new ContextDataType<>(tag -> new BooleanContextDataType(
                    tag.getBoolean("Value")));

    public static final ContextDataType<FloatContextDataType> FLOAT_TYPE =
            new ContextDataType<>(tag -> new FloatContextDataType(
                    tag.getFloat("Value")));


    public static void register(DeferredRegister<ContextDataType<?>> registry) {
        registry.register("default", () -> DEFAULT_TYPE);
        registry.register("position", () -> POSITION_TYPE);
        registry.register("player", () -> PLAYER_TYPE);
        registry.register("player_list", () -> PLAYER_LIST_TYPE);
        registry.register("boolean", () -> BOOLEAN_TYPE);
        registry.register("float", () -> FLOAT_TYPE);
    }
}
