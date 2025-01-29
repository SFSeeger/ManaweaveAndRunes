package io.github.sfseeger.manaweave_and_runes.core.init;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.menus.ManaStorageBlockMenu;
import io.github.sfseeger.manaweave_and_runes.common.menus.RuneCarverBlockMenu;
import io.github.sfseeger.manaweave_and_runes.common.menus.SpellDesignerMenu;
import io.github.sfseeger.manaweave_and_runes.common.menus.WandModificationTableMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ManaweaverAndRunesMenuInit {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU,
                                                                                      ManaweaveAndRunes.MODID);

    public static final Supplier<MenuType<RuneCarverBlockMenu>> RUNE_CARVER_BLOCK_MENU = MENUS.register(
            "rune_carver_menu", () -> new MenuType<>(RuneCarverBlockMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<ManaStorageBlockMenu>> MANA_STORAGE_BLOCK_MENU = MENUS.register(
            "mana_storage_menu", () -> IMenuTypeExtension.create(ManaStorageBlockMenu::new));
    public static final Supplier<MenuType<WandModificationTableMenu>> WAND_MODIFICARION_TABLE_MENU = MENUS.register(
            "wand_modification_table_menu", () -> IMenuTypeExtension.create(WandModificationTableMenu::new));
    public static final Supplier<MenuType<SpellDesignerMenu>> SPELL_DESIGNER_MENU = MENUS.register(
            "spell_designer_menu", () -> IMenuTypeExtension.create(SpellDesignerMenu::new));
}
