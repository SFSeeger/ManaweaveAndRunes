package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.menus.RuneCarverBlockMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ManaweaverAndRunesMenuInit {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU,
                                                                                      ManaweaveAndRunes.MODID);

    public static final Supplier<MenuType<RuneCarverBlockMenu>> RUNE_CARVER_BLOCK_MENU = MENUS.register(
            "rune_carver_menu", () -> new MenuType<>(RuneCarverBlockMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
