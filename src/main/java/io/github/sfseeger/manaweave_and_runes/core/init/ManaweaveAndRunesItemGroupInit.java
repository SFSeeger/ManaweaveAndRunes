package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ManaweaveAndRunesItemGroupInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ManaweaveAndRunes.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MANAWEAVE_AND_RUNES =
            CREATIVE_MODE_TABS.register("manaweave_and_runes_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.manaweave_and_runes"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> new ItemStack(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.get()))// TODO: change item
                    .displayItems((parameters, output) -> {
                        ManaweaveAndRunesItemInit.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
//                        output.accept(ManaweaveAndRunesItemInit.CRYSTAL_ORE_ITEM.get());
//                        output.accept(ManaweaveAndRunesItemInit.CRYSTAL.get());
//                        output.accept(ManaweaveAndRunesItemInit.RUNE_BLOCK_ITEM.get());
//                        output.accept(ManaweaveAndRunesItemInit.MANA_GENERATOR_BLOCK_ITEM.get());
//                        output.accept(ManaweaveAndRunesItemInit.MANA_COLLECTOR_BLOCK_ITEM.get());
//                        output.accept(ManaweaveAndRunesItemInit.AMETHYST_BASE_RUNE.get());
//                        output.accept(ManaweaveAndRunesItemInit.AMETHYST_FIRE_RUNE_ITEM.get());
//                        output.accept(ManaweaveAndRunesItemInit.AMETHYST_AIR_RUNE_ITEM.get());
//                        output.accept(ManaweaveAndRunesItemInit.DIAMOND_CHISEL.get());
                    }).build());
}