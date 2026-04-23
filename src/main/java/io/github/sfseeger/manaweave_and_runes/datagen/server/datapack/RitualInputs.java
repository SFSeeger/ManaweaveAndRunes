package io.github.sfseeger.manaweave_and_runes.datagen.server.datapack;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualInput;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.RitualInit;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class RitualInputs {
    public static final ResourceKey<RitualInput> SMITE_RITUAL = registerKey(RitualInit.SMITE_RITUAL.get());
    public static final ResourceKey<RitualInput> THUNDER_RITUAL = registerKey(RitualInit.THUNDER_RITUAL.get());
    public static final ResourceKey<RitualInput> TELEPORT_RITUAL = registerKey(RitualInit.TELEPORT_RITUAL.get());
    public static final ResourceKey<RitualInput> GROWTH_RITUAL = registerKey(RitualInit.GROWTH_RITUAL.get());
    public static final ResourceKey<RitualInput> SANCTUARY = registerKey(RitualInit.SANCTUARY_RITUAL.get());
    public static final ResourceKey<RitualInput> ASCENDED_SANCTUARY =
            registerKey(RitualInit.ASCENDED_SANCTUARY_RITUAL.get());
    public static final ResourceKey<RitualInput> FLIGHT_RITUAL = registerKey(RitualInit.FLIGHT_RITUAL.get());
    public static final ResourceKey<RitualInput> SHATTERING_RITE = registerKey(RitualInit.SHATTERING_RITE_RITUAL.get());
    public static final ResourceKey<RitualInput> MARK_OF_OVERHEATING =
            registerKey(RitualInit.MARK_OF_OVERHEATING_RITUAL.get());
    public static final ResourceKey<RitualInput> CURSE_REMOVAL_RITUAL =
            registerKey(RitualInit.CURSE_REMOVAL_RITUAL.get());



    public static void bootsrap(BootstrapContext<RitualInput> context) {
        context.register(registerKey(RitualInit.PARTICLE_RITUAL.get()), new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.GLOW_INK_SAC))
                .addInitialItemCost(Ingredient.of(Items.FEATHER))
                .addManaCost(Manas.AirMana, 10)
                .setManaRate(20)
                .setItemRate(20)
                .build()
        );

        context.register(SMITE_RITUAL, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.LIGHTNING_ROD))
                .addInitialItemCost(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addManaCost(Manas.AirMana, 10)
                .addManaCost(Manas.FireMana, 4)
                .setManaRate(20)
                .build()
        );

        context.register(THUNDER_RITUAL, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.LIGHTNING_ROD))
                .addInitialItemCost(Ingredient.of(Items.BLAZE_ROD))
                .addInitialItemCost(Ingredient.of(MRItemInit.POSITION_RUNE_ITEM))
                .addTickItemCost(Ingredient.of(Items.BLAZE_POWDER))
                .addManaCost(Manas.OrderMana, 8)
                .addManaCost(Manas.EarthMana, 10)
                .addManaCost(Manas.WaterMana, 1)
                .setManaRate(20)
                .setItemRate(20)
                .build()
        );

        context.register(GROWTH_RITUAL, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.BONE_MEAL))
                .addInitialItemCost(Ingredient.of(ItemTags.VILLAGER_PLANTABLE_SEEDS))
                .addInitialItemCost(Ingredient.of(ItemTags.SAPLINGS))
                .addManaCost(Manas.OrderMana, 8)
                .addManaCost(Manas.EarthMana, 10)
                .addManaCost(Manas.WaterMana, 1)
                .setManaRate(20)
                .build()
        );

        context.register(SANCTUARY, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.GOLDEN_APPLE))
                .addInitialItemCost(Ingredient.of(Items.IRON_SWORD))
                .addInitialItemCost(Ingredient.of(Items.WIND_CHARGE))
                .addInitialItemCost(Ingredient.of(Items.GOLD_INGOT))
                .addManaCost(Manas.OrderMana, 10)
                .addManaCost(Manas.AirMana, 25)
                .setManaRate(60)
                .build()
        );
        context.register(ASCENDED_SANCTUARY, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.GOLD_INGOT))
                .addInitialItemCost(Ingredient.of(Items.DIAMOND_SWORD))
                .addInitialItemCost(Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE))
                .addInitialItemCost(Ingredient.of(Items.WIND_CHARGE))
                .addInitialItemCost(Ingredient.of(Items.GLOWSTONE))
                .addManaCost(Manas.OrderMana, 10)
                .addManaCost(Manas.AirMana, 25)
                .addManaCost(Manas.SoulMana, 3)
                .setManaRate(60)
                .build()
        );
        context.register(FLIGHT_RITUAL, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.FEATHER))
                .addInitialItemCost(Ingredient.of(Items.FEATHER))
                .addInitialItemCost(Ingredient.of(Items.WIND_CHARGE))
                .addManaCost(Manas.AirMana, 20)
                .addManaCost(Manas.SoulMana, 5)
                .setManaRate(40)
                .build()
        );
        context.register(SHATTERING_RITE, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.DIAMOND))
                .addInitialItemCost(Ingredient.of(Items.DIAMOND_PICKAXE))
                .addInitialItemCost(Ingredient.of(MRItemInit.POSITION_RUNE_ITEM))
                .addManaCost(Manas.EarthMana, 15)
                .addManaCost(Manas.EntropyMana, 6)
                .setManaRate(60)
                .build()
        );
        context.register(TELEPORT_RITUAL, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.ENDER_PEARL))
                .addInitialItemCost(Ingredient.of(Items.ENDER_EYE))
                .addInitialItemCost(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addInitialItemCost(Ingredient.of(Items.BLAZE_POWDER))
                .addInitialItemCost(Ingredient.of(Items.BLAZE_ROD))
                .addManaCost(Manas.OrderMana, 5)
                .addManaCost(Manas.AirMana, 10)
                .setManaRate(10)
                .build()
        );

        context.register(MARK_OF_OVERHEATING, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.BLAZE_POWDER))
                .addInitialItemCost(Ingredient.of(Items.BLAZE_ROD))
                .addInitialItemCost(Ingredient.of(MRItemInit.MARK_CONTAINER_ITEM))
                .addInitialItemCost(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addManaCost(Manas.FireMana, 200)
                .addManaCost(Manas.EntropyMana, 50)
                .addManaCost(Manas.SoulMana, 50)
                .setManaRate(20)
                .build()
        );
        context.register(registerKey(RitualInit.MARK_OF_SINKING_RITUAL.get()), new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.OBSIDIAN))
                .addInitialItemCost(Ingredient.of(Items.DEEPSLATE))
                .addInitialItemCost(Ingredient.of(Items.NETHER_WART))
                .addInitialItemCost(Ingredient.of(Items.DIAMOND))
                .addInitialItemCost(Ingredient.of(MRItemInit.MARK_CONTAINER_ITEM))
                .addInitialItemCost(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addManaCost(Manas.EarthMana, 300)
                .addManaCost(Manas.EntropyMana, 500)
                .addManaCost(Manas.SoulMana, 500)
                .setManaRate(20)
                .build()
        );
        context.register(registerKey(RitualInit.MARK_OF_GROWTH_RITUAL.get()), new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.BONE_BLOCK))
                .addInitialItemCost(Ingredient.of(Items.DIAMOND))
                .addInitialItemCost(Ingredient.of(Items.NETHER_WART))
                .addInitialItemCost(Ingredient.of(MRItemInit.MARK_CONTAINER_ITEM))
                .addInitialItemCost(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addManaCost(Manas.WaterMana, 300)
                .addManaCost(Manas.EntropyMana, 500)
                .addManaCost(Manas.SoulMana, 500)
                .setManaRate(20)
                .build()
        );

        context.register(registerKey(RitualInit.MARK_OF_SHRINKING_RITUAL.get()), new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.RABBIT_FOOT))
                .addInitialItemCost(Ingredient.of(Items.DIAMOND))
                .addInitialItemCost(Ingredient.of(Items.NETHER_WART))
                .addInitialItemCost(Ingredient.of(MRItemInit.MARK_CONTAINER_ITEM))
                .addInitialItemCost(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addManaCost(Manas.EarthMana, 300)
                .addManaCost(Manas.EntropyMana, 500)
                .addManaCost(Manas.SoulMana, 500)
                .setManaRate(20)
                .build()
        );
        context.register(registerKey(RitualInit.MARK_OF_FLYING_RITUAL.get()), new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.ELYTRA))
                .addInitialItemCost(Ingredient.of(MRBlockInit.TANZANITE_BLOCK))
                .addInitialItemCost(Ingredient.of(Items.NETHER_STAR))
                .addInitialItemCost(Ingredient.of(Items.NETHERITE_INGOT))
                .addInitialItemCost(Ingredient.of(Items.NETHERITE_INGOT))
                .addInitialItemCost(Ingredient.of(MRItemInit.MARK_CONTAINER_ITEM))
                .addInitialItemCost(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addManaCost(Manas.AirMana, 500)
                .addManaCost(Manas.EarthMana, 500)
                .addManaCost(Manas.VoidMana, 500)
                .addManaCost(Manas.SoulMana, 500)
                .addManaCost(Manas.OrderMana, 500)
                .addManaCost(Manas.WaterMana, 500)
                .setManaRate(20)
                .build()
        );


        context.register(CURSE_REMOVAL_RITUAL, new RitualInput.Builder()
                .addInitialItemCost(Ingredient.of(Items.AMETHYST_BLOCK))
                .addInitialItemCost(Ingredient.of(Items.BLAZE_ROD))
                .addInitialItemCost(Ingredient.of(Items.LAPIS_LAZULI))
                .addInitialItemCost(Ingredient.of(MRItemInit.SOUL_CONTAINER_RUNE_ITEM))
                .addManaCost(Manas.FireMana, 15)
                .addManaCost(Manas.EntropyMana, 5)
                .setManaRate(20)
                .build()
        );
    }

    private static ResourceKey<RitualInput> registerKey(Ritual ritual) {
        return ResourceKey.create(ManaweaveAndRunesRegistries.RITUAL_INPUT_REGISTRY_KEY, ritual.getRegistryName());
    }
}
