package io.github.sfseeger.lib.common.context_data_types.builtin;

import io.github.sfseeger.lib.common.context_data_types.IContextDataType;
import io.github.sfseeger.lib.common.context_data_types.ContextDataType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import static io.github.sfseeger.lib.common.context_data_types.ContextDataTypes.POSITION_TYPE;

public record PositionContextDataType(BlockPos pos) implements IContextDataType {

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("Position", pos.asLong());
        return tag;
    }

    @Override
    public ContextDataType<?> getType() {
        return POSITION_TYPE;
    }

    @Override
    public IContextDataType clone() throws CloneNotSupportedException {
        return (PositionContextDataType) super.clone();
    }
}
