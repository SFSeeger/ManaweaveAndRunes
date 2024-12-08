package io.github.sfseeger.lib.mana.capability;

import io.github.sfseeger.lib.mana.Mana;
import io.github.sfseeger.lib.mana.ManaRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ManaHandler implements IManaHandler, INBTSerializable {
    protected List<Mana> allowedMana = new ArrayList<>();
    protected int fallbackCapacity = 0;
    protected HashMap<Mana, Integer> manaCapacity = new HashMap<>();
    protected HashMap<Mana, Integer> manaStored = new HashMap<>();
    protected int maxManaReceive = 0;
    protected int maxManaExtract = 0;

    public ManaHandler(Mana manatype, int capacity, int stored, int maxReceive, int maxExtract) {
        allowedMana = List.of(manatype);
        this.manaCapacity.put(manatype, capacity);
        this.manaStored.put(manatype, stored);
        this.maxManaReceive = maxReceive;
        this.maxManaExtract = maxExtract;
    }

    public ManaHandler(HashMap<Mana, Integer> manaCapacity, HashMap<Mana, Integer> manaStored,
            int maxManaReceive, int maxManaExtract, int fallbackCapacity,
            @Nullable List<Mana> allowedMana) {
        if (allowedMana != null) {
            this.allowedMana = allowedMana;
        }
        this.fallbackCapacity = fallbackCapacity;
        this.manaCapacity = manaCapacity;
        this.manaStored = manaStored;
        this.maxManaReceive = maxManaReceive;
        this.maxManaExtract = maxManaExtract;
    }

    public ManaHandler(HashMap<Mana, Integer> manaCapacity, HashMap<Mana, Integer> manaStored,
            int maxManaReceive, int maxManaExtract,
            @Nullable List<Mana> allowedMana) {
        if (allowedMana != null) {
            this.allowedMana = allowedMana;
        }
        this.manaCapacity = manaCapacity;
        this.manaStored = manaStored;
        this.maxManaReceive = maxManaReceive;
        this.maxManaExtract = maxManaExtract;
    }

    public ManaHandler(int fallbackCapacity, int maxManaExtract, int maxManaReceive, @Nullable List<Mana> allowedMana) {
        if (allowedMana != null) {
            this.allowedMana = allowedMana;
        }
        this.fallbackCapacity = fallbackCapacity;
        this.maxManaReceive = maxManaReceive;
        this.maxManaExtract = maxManaExtract;
    }


    @Override
    public int receiveMana(int amount, Mana manatype, boolean simulate) {
        if (this.canReceive(manatype) && amount > 0) {
            if (!this.hasMana(manatype)) {
                this.manaStored.put(manatype, 0);
                this.manaCapacity.put(manatype, this.fallbackCapacity);
            }
            int space = this.getMaxManaStored(manatype) - this.getManaStored(manatype);
            int accepted = Math.clamp(space, 0, Math.min(this.maxManaReceive, amount));
            if (!simulate) {
                this.manaStored.put(manatype, this.getManaStored(manatype) + accepted);
                onContentChanged();
            }
            return accepted;
        }
        return 0;
    }

    @Override
    public int extractMana(int amount, Mana manatype, boolean simulate) {
        if (this.canExtract(manatype) && amount > 0) {
            int extracted = Math.min(this.getManaStored(manatype), Math.min(this.maxManaExtract, amount));
            if (!simulate) {
                this.manaStored.put(manatype, this.getManaStored(manatype) - extracted);
                onContentChanged();
            }
            return extracted;
        }
        return 0;
    }

    @Override
    public int getManaStored(Mana manatype) {
        Integer stored = manaStored.get(manatype);
        return stored == null ? 0 : stored;
    }

    @Override
    public int getMaxManaStored(Mana manatype) {
        Integer capacity = manaCapacity.get(manatype);
        return capacity == null ? -1 : capacity;
    }

    @Override
    public boolean canExtract(Mana manatype) {
        if (!allowedMana.isEmpty() && !allowedMana.contains(manatype)) {
            return false;
        }
        return maxManaExtract > 0;
    }

    @Override
    public boolean canReceive(Mana manatype) {
        if (!allowedMana.isEmpty() && !allowedMana.contains(manatype)) {
            return false;
        }
        return maxManaReceive > 0;
    }

    @Override
    public Set<Mana> getManaTypesStored() {
        return manaStored.keySet();
    }

    @Override
    public boolean hasMana(Mana manatype) {
        return manaStored.containsKey(manatype);
    }

    public void onContentChanged() {
    }

    @Override
    public Tag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Mana manatype : manaCapacity.keySet()) {
            tag.putInt(ManaRegistry.MANA_REGISTRY.getKey(manatype).toString(), manaCapacity.get(manatype));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        CompoundTag compoundTag = (CompoundTag) tag;
        if (tag instanceof CompoundTag) {
            for (String manaType : compoundTag.getAllKeys()) {
                Mana mana = ManaRegistry.MANA_REGISTRY.get(ResourceLocation.parse(manaType));
                if (mana == null || !((CompoundTag) tag).contains(manaType)) {
                    continue;
                }
                IntTag intTag = (IntTag) compoundTag.get(manaType);
                manaCapacity.put(mana, intTag.getAsInt());
            }
        } else {
            throw new IllegalArgumentException("Expected CompoundTag, got " + tag.getClass());
        }
    }
}
