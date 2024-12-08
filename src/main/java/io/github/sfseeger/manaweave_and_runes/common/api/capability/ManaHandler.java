package io.github.sfseeger.manaweave_and_runes.common.api.capability;

import io.github.sfseeger.manaweave_and_runes.common.api.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.common.api.mana.ManaRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Map;

public class ManaHandler implements IManaHandler, INBTSerializable {
    protected Map<Mana, Integer> manaCapacity;
    protected Map<Mana, Integer> manaStored;
    protected Map<Mana, Integer> maxManaReceive;
    protected Map<Mana, Integer> maxManaExtract;

    public ManaHandler(Mana manatype, int capacity, int stored, int maxReceive, int maxExtract){
        this.manaCapacity.put(manatype, capacity);
        this.manaStored.put(manatype, stored);
        this.maxManaReceive.put(manatype, maxReceive);
        this.maxManaExtract.put(manatype, maxExtract);
    }

    public ManaHandler(Map<Mana, Integer> manaCapacity, Map<Mana, Integer> manaStored, Map<Mana, Integer> maxManaReceive, Map<Mana, Integer> maxManaExtract){
        this.manaCapacity = manaCapacity;
        this.manaStored = manaStored;
        this.maxManaReceive = maxManaReceive;
        this.maxManaExtract = maxManaExtract;
    }


    @Override
    public int receiveMana(int amount, Mana manatype, boolean simulate) {
        if(this.canReceive(manatype) && amount > 0){
            int space = this.getMaxManaStored(manatype) - this.getManaStored(manatype);
            int accepted = Math.clamp(space, 0, Math.min(this.maxManaReceive.get(manatype), amount));
            if(!simulate){
                this.manaStored.put(manatype, this.getManaStored(manatype) + accepted);
            }
            return accepted;
        }
        return 0;
    }

    @Override
    public int extractMana(int amount, Mana manatype, boolean simulate) {
        if(this.canExtract(manatype) && amount > 0){
            int extracted = Math.min(this.getManaStored(manatype), Math.min(this.maxManaExtract.get(manatype), amount));
            if(!simulate){
                this.manaStored.put(manatype, this.getManaStored(manatype) - extracted);
            }
            return extracted;
        }
        return 0;
    }

    @Override
    public int getManaStored(Mana manatype) {
        return manaStored.get(manatype);
    }

    @Override
    public int getMaxManaStored(Mana manatype) {
        return manaCapacity.get(manatype);
    }

    @Override
    public boolean canExtract(Mana manatype) {
        return maxManaExtract.get(manatype) > 0;
    }

    @Override
    public boolean canReceive(Mana manatype) {
        return maxManaReceive.get(manatype) > 0;
    }

    @Override
    public Tag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for(Mana manatype : manaCapacity.keySet()) {
            tag.putInt(ManaRegistry.MANA_REGISTRY.getKey(manatype).toString(), manaCapacity.get(manatype));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        CompoundTag compoundTag = (CompoundTag) tag;
        if(tag instanceof CompoundTag){
            for(String manaType : compoundTag.getAllKeys()){
                Mana mana = ManaRegistry.MANA_REGISTRY.get(ResourceLocation.parse(manaType));
                if(mana == null || !((CompoundTag) tag).contains(manaType)){
                    continue;
                }
                IntTag intTag = (IntTag) compoundTag.get(manaType);
                manaCapacity.put(mana, intTag.getAsInt());
            }
        }
        else {
            throw new IllegalArgumentException("Expected CompoundTag, got " + tag.getClass());
        }
    }
}
