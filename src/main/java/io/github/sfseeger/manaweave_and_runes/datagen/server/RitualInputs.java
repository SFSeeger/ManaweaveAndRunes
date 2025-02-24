package io.github.sfseeger.manaweave_and_runes.datagen.server;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualInput;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.core.init.RitualInit;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class RitualInputs {
    public static final ResourceKey<RitualInput> GROWTH_RITUAL = registerKey(RitualInit.GROWTH_RITUAL.get());

    public static void bootsrap(BootstrapContext<RitualInput> context) {
        context.register(GROWTH_RITUAL, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.BONE_MEAL))
                .addInitialItemCost(Ingredient.of(ItemTags.VILLAGER_PLANTABLE_SEEDS))
                .addInitialItemCost(Ingredient.of(ItemTags.SAPLINGS))
                .addManaCost(Manas.AirMana, 5)
                .setManaRate(20)
                .build()
        );
    }

    private static ResourceKey<RitualInput> registerKey(Ritual ritual) {
        return ResourceKey.create(ManaweaveAndRunesRegistries.RITUAL_INPUT_REGISTRY_KEY, ritual.getRegistryName());
    }
}
