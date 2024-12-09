package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ManaweaveAndRunesItemGroupInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ManaweaveAndRunes.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MANAWEAVE_AND_RUNES =
            CREATIVE_MODE_TABS.register("manaweave_and_runes_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.manaweave_and_runes"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> Items.DIRT.getDefaultInstance())// TODO: change item
                    .displayItems((parameters, output) -> {
                        output.accept(ManaweaveAndRunesItemInit.CRYSTAL_ORE_ITEM.get());
                        output.accept(ManaweaveAndRunesItemInit.MANA_STORAGE_BLOCK_ITEM.get());
                        output.accept(ManaweaveAndRunesItemInit.MANA_COLLECTOR_BLOCK_ITEM.get());
                    }).build());
}