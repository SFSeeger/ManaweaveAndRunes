package io.github.sfseeger.lib.common.rituals;

import io.github.sfseeger.lib.common.LibUtils;
import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepId;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IRitualManager {
    default void startRitual(Ritual ritual) {
        setRitual(ritual);
        markUpdated();
    }

    default Tag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("state", getState().ordinal());
        tag.put("ritual", LibUtils.encode(Ritual.CODEC, getRitual(), provider));

        return tag;
    }

    default void deserializeNBT(CompoundTag tag, HolderLookup.Provider holderLookup) {
        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, holderLookup);

        int state = tag.contains("state") ? tag.getInt("state") : RitualStepId.IDLE.ordinal();
        setState(RitualStepId.values()[state]);
        setRitual(Ritual.CODEC.parse(ops, tag.get("ritual")).result().orElse(null));
    }

    void markUpdated();

    Ritual getRitual();

    void setRitual(Ritual ritual);

    RitualStepId getState();

    void setState(RitualStepId state);

    default Optional<Ritual> getMatchingRitual(List<ItemStack> items, Tier tier, Ritual.RitualOriginType originType, Level level) {
        return ManaweaveAndRunesRegistries.RITUAL_REGISTRY.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(ritual -> ritual.matches(items, tier, originType, level))
                .findFirst();
    }
}