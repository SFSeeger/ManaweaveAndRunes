package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.util.ICreativeTabItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ManaweaveAndRunesItemGroupInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ManaweaveAndRunes.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MANAWEAVE_AND_RUNES =
            CREATIVE_MODE_TABS.register("manaweave_and_runes_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.manaweave_and_runes"))
                    .icon(() -> new ItemStack(MRItemInit.AMETHYST_AIR_RUNE_ITEM.get()))
                    .displayItems((parameters, output) -> {
                        MRItemInit.ITEMS.getEntries().forEach(item -> {
                            if (item.get() instanceof ICreativeTabItem creativeTabItem) {
                                if (creativeTabItem.isInCreativeTab()) {
                                    output.accept(item.get());
                                }
                            } else if (item.get().asItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ICreativeTabItem creativeTabBlock) {
                                if (creativeTabBlock.isInCreativeTab()) {
                                    output.accept(item.get());
                                }
                            }
                            else {
                                output.accept(item.get());
                            }
                        });
                    })
                    .build());
}