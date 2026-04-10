package io.github.sfseeger.manaweave_and_runes.core.init;

import com.google.common.collect.ImmutableSet;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MRVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, ManaweaveAndRunes.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, ManaweaveAndRunes.MODID);

    public static final Holder<PoiType> RUNE_SMITH_POI = POI_TYPES.register("rune_smith_poi", () -> new PoiType(
            ImmutableSet.copyOf(MRBlockInit.RUNE_CARVER_BLOCK.get().getStateDefinition().getPossibleStates()), 1, 1));


    public static final Holder<VillagerProfession> RUNE_SMITH =
            VILLAGER_PROFESSIONS.register("rune_smith", () -> new VillagerProfession(
                    "rune_smith",
                    holder -> holder.value() == RUNE_SMITH_POI.value(),
                    poiTypeHolder -> poiTypeHolder.value() == RUNE_SMITH_POI.value(),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_WEAPONSMITH)
            );

    public static void register(IEventBus bus) {
        POI_TYPES.register(bus);
        VILLAGER_PROFESSIONS.register(bus);
    }
}
