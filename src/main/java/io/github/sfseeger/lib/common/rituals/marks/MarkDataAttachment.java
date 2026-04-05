package io.github.sfseeger.lib.common.rituals.marks;

import io.github.sfseeger.lib.common.LibUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MarkDataAttachment implements INBTSerializable<CompoundTag> {
    private static final List<MarkInstance> MARK_INSTANCES = new ArrayList<>();

    public List<MarkInstance> getMarks() {
        return MARK_INSTANCES;
    }
    public List<MarkInstance> getCurses() {
        return MARK_INSTANCES.stream().filter(instance -> instance.getMarkType() == MarkType.CURSE).toList();
    }
    public List<MarkInstance> getBoons() {
        return MARK_INSTANCES.stream().filter(instance -> instance.getMarkType() == MarkType.BOON).toList();
    }


    public void addMark(MarkInstance markInstance) {
        MARK_INSTANCES.add(markInstance);
    }

    public void removeMark(MarkInstance markInstance) {MARK_INSTANCES.remove(markInstance);}

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag marksList = new ListTag();
        for (MarkInstance markInstance : MARK_INSTANCES) {
            marksList.add(LibUtils.encode(MarkInstance.CODEC, markInstance, provider));
        }
        tag.put("Marks", marksList);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        MARK_INSTANCES.clear();
        ListTag markList = compoundTag.getList("Marks", ListTag.TAG_COMPOUND);
        for (Tag t : markList) {
            MarkInstance m = LibUtils.decode(MarkInstance.CODEC, t, provider);
            MARK_INSTANCES.add(m);
        }
    }
}
