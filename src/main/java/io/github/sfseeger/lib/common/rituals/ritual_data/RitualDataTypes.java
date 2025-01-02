package io.github.sfseeger.lib.common.rituals.ritual_data;

import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PositionRitualData;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RitualDataTypes {
    public static final RitualDataType<?> DEFAULT_TYPE = new RitualDataType<>(tag -> null);

    public static final RitualDataType<PositionRitualData> POSITION_TYPE =
            new RitualDataType<>(tag -> new PositionRitualData(
                    BlockPos.of(tag.getLong("Position"))));

    public static final RitualDataType<PlayerRitualData> PLAYER_TYPE =
            new RitualDataType<>(tag -> new PlayerRitualData(
                    tag.getString("PlayerUUID")));

    public static void register(DeferredRegister<RitualDataType<?>> registry) {
        registry.register("default", () -> DEFAULT_TYPE);
        registry.register("position", () -> POSITION_TYPE);
        registry.register("player", () -> PLAYER_TYPE);
    }
}
