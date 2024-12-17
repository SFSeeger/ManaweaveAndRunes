package io.github.sfseeger.lib.common.mana.capability;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.ManaRegistry;
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
import java.util.function.Supplier;

public class ManaHandler implements IManaHandler, INBTSerializable {
    protected List<Supplier<Mana>> allowedMana;
    protected HashMap<Mana, Integer> manaStored = new HashMap<>();
    protected int maxManaReceive = 0;
    protected int maxManaExtract = 0;
    protected int capacity = 0;

    public ManaHandler(int capacity, int maxManaReceive, int maxManaExtract, @Nullable List<Supplier<Mana>> allowedMana) {
        this.maxManaReceive = maxManaReceive;
        this.maxManaExtract = maxManaExtract;
        this.capacity = capacity;
        this.allowedMana = allowedMana != null ? allowedMana : new ArrayList<>();
    }


    @Override
    public int receiveMana(int amount, Mana manatype, boolean simulate) {
        if (this.canReceive(manatype) && amount > 0) {
            int space = this.capacity - this.getManaStored(manatype);
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
                int result = this.getManaStored(manatype) - extracted;
                this.manaStored.put(manatype, result);
                if (result == 0 && allowedMana.isEmpty()) {
                    this.manaStored.remove(manatype);
                }
                onContentChanged();
            }
            return extracted;
        }
        return 0;
    }

    protected void setMana(Mana manatype, int amount) {
        if (amount > 0) {
            this.manaStored.put(manatype, amount);
        }
    }

    @Override
    public int getManaStored(Mana manatype) {
        Integer stored = manaStored.get(manatype);
        return stored == null ? -1 : stored;
    }

    @Override
    public int getManaCapacity() {
        return capacity;
    }

    @Override
    public boolean canExtract(Mana manatype) {
        if (!allowedManaContains(manatype)) {
            return false;
        }
        return maxManaExtract > 0;
    }

    @Override
    public boolean canReceive(Mana manatype) {
        if (!allowedManaContains(manatype)) {
            return false;
        }
        return maxManaReceive > 0;
    }

    private boolean allowedManaContains(Mana manatype) {
        return allowedMana.isEmpty() || allowedMana.stream().anyMatch(manaSupplier -> manaSupplier.get().equals(manatype));
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
        for (Mana manatype : manaStored.keySet()) {
            Integer stored = manaStored.get(manatype);
            tag.putInt(ManaRegistry.MANA_REGISTRY.getKey(manatype).toString(), stored == null ? 0 : stored);
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
                manaStored.put(mana, intTag.getAsInt());
            }
        } else {
            throw new IllegalArgumentException("Expected CompoundTag, got " + tag.getClass());
        }
    }
}
