package io.github.sfseeger.lib.common.spells.casting_context;

import io.github.sfseeger.lib.common.LibUtils;
import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.context_data_types.IContextDataType;
import io.github.sfseeger.lib.common.spells.AbstractSpellCastingContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockCasterCastingContext extends AbstractSpellCastingContext {
    private Vec3 position;
    private BlockPos pos;
    private Vec3 lookDirection;
    private Direction direction;
    private @Nullable Level level;

    public BlockCasterCastingContext(BlockPos pos, Vec3 lookDirection, Direction direction, @Nullable Level level) {
        this.position = Vec3.atCenterOf(pos);
        this.pos = pos;
        this.lookDirection = lookDirection;
        this.direction = direction;
        this.level = level;
    }

    @Override
    public Vec3 getPosition() {
        return position;
    }

    @Override
    public Vec3 getLookDirection() {
        return lookDirection;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public @Nullable Level getLevel() {
        return level;
    }

    @Override
    public ContextMap getContextData() {
        return contextData;
    }

    @Override
    public AbstractSpellCastingContext clone() throws CloneNotSupportedException {
        BlockCasterCastingContext clone = (BlockCasterCastingContext) super.clone();
        clone.contextData.clear();
        for (Map.Entry<String, IContextDataType> entry : contextData) {
            clone.getContextData().putData(entry.getKey(), entry.getValue().clone());
        }
        return clone;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = super.serializeNBT(provider);
        tag.put("Position", LibUtils.encode(Vec3.CODEC, position, provider));
        tag.put("LookDirection", LibUtils.encode(Vec3.CODEC, lookDirection, provider));
        tag.put("Direction", LibUtils.encode(Direction.CODEC, direction, provider));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        super.deserializeNBT(provider, tag);
        this.position = LibUtils.decode(Vec3.CODEC, tag.get("Position"), provider);
        this.pos = BlockPos.containing(this.position);
        this.lookDirection = LibUtils.decode(Vec3.CODEC, tag.get("LookDirection"), provider);
        this.direction = LibUtils.decode(Direction.CODEC, tag.get("Direction"), provider);
    }

    @Override
    public void loadLevelDependentData(Level level) {
        this.level = level;
    }
}
