package io.github.sfseeger.lib.common.rituals.ritual_data.builtin;

import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualData;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.POSITION_TYPE;

public class PositionRitualData implements IRitualData {
    private final BlockPos pos;

    public PositionRitualData(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("Position", pos.asLong());
        return tag;
    }

    @Override
    public RitualDataType<?> getType() {
        return POSITION_TYPE;
    }
}
