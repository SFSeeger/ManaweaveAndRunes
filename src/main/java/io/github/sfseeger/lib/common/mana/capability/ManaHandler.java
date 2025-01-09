package io.github.sfseeger.lib.common.mana.capability;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
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
import java.util.function.Supplier;

public class ManaHandler implements IManaHandler, INBTSerializable {
    protected List<Supplier<Mana>> allowedMana;
    protected HashMap<Mana, Integer> manaStored = new HashMap<>();
    protected int maxManaReceive;
    protected int maxManaExtract;
    protected int capacity;
    protected int slots = -1;

    public ManaHandler(int capacity, int maxManaReceive, int maxManaExtract, @Nullable List<Supplier<Mana>> allowedMana) {
        this.maxManaReceive = maxManaReceive;
        this.maxManaExtract = maxManaExtract;
        this.capacity = capacity;
        this.allowedMana = allowedMana != null ? allowedMana : new ArrayList<>();
    }

    public ManaHandler(int capacity, int maxManaReceive, int maxManaExtract, int slots,
            @Nullable List<Supplier<Mana>> allowedMana) {
        this.maxManaReceive = maxManaReceive;
        this.maxManaExtract = maxManaExtract;
        this.capacity = capacity;
        this.allowedMana = allowedMana != null ? allowedMana : new ArrayList<>();
        if (slots > 0) {
            this.slots = slots;
            if (!this.allowedMana.isEmpty() && this.allowedMana.size() != slots) throw new IllegalArgumentException(
                    "ManaHandler slots must be equal to the size of the allowedMana list");
        }
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
        return stored == null ? 0 : stored;
    }

    @Override
    public int getManaCapacity() {
        return capacity;
    }

    @Override
    public boolean canExtract(Mana manatype) {
        if (!allowedManaContains(manatype) || (slots > 0 && manaStored.size() >= slots)) {
            return false;
        }
        return maxManaExtract > 0;
    }

    @Override
    public boolean canReceive(Mana manatype) {
        if (!allowedManaContains(manatype) || (slots > 0 && manaStored.size() >= slots)) {
            return false;
        }
        return maxManaReceive > 0;
    }

    private boolean allowedManaContains(Mana manatype) {
        return allowedMana.isEmpty() || allowedMana.stream().anyMatch(manaSupplier -> manaSupplier.get().equals(manatype));
    }

    @Override
    public List<Mana> getManaTypesStored() {
        return manaStored.keySet().stream().toList();
    }

    @Override
    public boolean hasMana(Mana manatype) {
        return manaStored.containsKey(manatype);
    }

    public void onContentChanged() {
    }

    public List<Supplier<Mana>> getAllowedMana() {
        return allowedMana;
    }

    public void setAllowedMana(List<Supplier<Mana>> allowedMana) {
        this.allowedMana = allowedMana;
    }

    @Override
    public Tag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Mana manatype : manaStored.keySet()) {
            Integer stored = manaStored.get(manatype);
            tag.putInt(ManaweaveAndRunesRegistries.MANA_REGISTRY.getKey(manatype).toString(),
                       stored == null ? 0 : stored);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        CompoundTag compoundTag = (CompoundTag) tag;
        if (tag instanceof CompoundTag) {
            for (String manaType : compoundTag.getAllKeys()) {
                Mana mana = ManaweaveAndRunesRegistries.MANA_REGISTRY.get(ResourceLocation.parse(manaType));
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
