package io.github.sfseeger.lib.common.spells;

import io.github.sfseeger.lib.common.context_data_types.ContextDataTypes;
import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.context_data_types.IContextDataType;
import io.github.sfseeger.lib.common.context_data_types.builtin.BooleanContextDataType;
import io.github.sfseeger.lib.common.context_data_types.builtin.FloatContextDataType;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public abstract class AbstractSpellCastingContext implements Cloneable, INBTSerializable<CompoundTag> {
    protected @Nullable LivingEntity caster;
    protected @Nullable Integer casterId;
    protected @Nullable UUID casterUUID;
    protected ContextMap contextData = new ContextMap();

    public abstract Vec3 getPosition();

    public abstract Vec3 getLookDirection();

    public abstract Direction getDirection();

    @Nullable
    public abstract Level getLevel();

    @Nullable
    public LivingEntity getCaster() {
        return caster;
    }

    public void setCaster(@Nullable LivingEntity entity) {
        this.caster = entity;
    }

    public @Nullable Integer getCasterId() {
        if (this.casterId == null && caster != null) {
            casterId = caster.getId();
        }
        return casterId;
    }

    public void setCasterId(@Nullable Integer id){
        this.casterId = id;
    }

    public @Nullable UUID getCasterUUID() {
        if (this.casterUUID == null && caster != null) {
            casterUUID = caster.getUUID();
        }
        return casterUUID;
    }

    public void setCasterUUID(@Nullable UUID uuid){
        this.casterUUID = uuid;
    }

    public abstract ContextMap getContextData();

    public void setContextData(ContextMap contextData) {
        this.contextData = contextData;
    }

    /**
     *  Returns a new Instance of AbstractSpellCastingContext while keeping global & relevant fields preserved.
     *  Global fields are marked using the "global_" key prefix
     *
     * @return AbstractSpellCastingContext
     */
    public AbstractSpellCastingContext intoFreshInstance() {
        try {
            AbstractSpellCastingContext clone = this.clone();
            this.getContextData().keySet().stream().filter(key -> key.startsWith("global")).forEach(key -> clone.getContextData().remove(key));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Failed to clone AbstractSpellCastingContext", e);
        }
    }

    public void deserializeNBT(HolderLookup.Provider lookup, CompoundTag tag, Level level) {
        this.deserializeNBT(lookup, tag);
        loadLevelDependentData(level);
    }

    public void loadLevelDependentData(Level level) {
        if (getCasterId() != null) {
            Entity entity = level.getEntity(getCasterId());
            if (entity != null && entity.isAlive() && entity instanceof LivingEntity livingEntity) {
                setCaster(livingEntity);
            }
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.put("Data", getContextData().serializeNBT(provider));
        if(getCasterId() != null) tag.putInt("CasterId", getCasterId());
        if(getCasterUUID() != null) tag.putUUID("CasterUUID", getCasterUUID());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.getContextData().deserializeNBT(provider, tag.getCompound("Data"));
        this.setCasterUUID(tag.getUUID("CasterUUID"));
        this.setCasterId(tag.getInt("CasterId"));
    }

    public float getFloatContextData(String key, float defaultValue) {
        return getContextData().getData(key, ContextDataTypes.FLOAT_TYPE)
                .map(FloatContextDataType::value)
                .orElse(defaultValue);
    }
    public float getFloatContextData(String key) {
        return getFloatContextData(key, 0f);
    }

    public boolean getBooleanContextData(String key, boolean defaultValue) {
        return getContextData().getData(key, ContextDataTypes.BOOLEAN_TYPE)
                .map(BooleanContextDataType::value)
                .orElse(defaultValue);
    }
    public boolean getBooleanContextData(String key) {
        return getBooleanContextData(key, false);
    }

    public AbstractSpellCastingContext clone() throws CloneNotSupportedException {
        AbstractSpellCastingContext clone = (AbstractSpellCastingContext) super.clone();
        clone.contextData.clear();
        for (Map.Entry<String, IContextDataType> entry : contextData) {
            clone.getContextData().putData(entry.getKey(), entry.getValue().clone());
        }
        return clone;
    }
}
