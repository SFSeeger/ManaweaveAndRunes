package io.github.sfseeger.lib.common.mana;

import io.github.sfseeger.lib.common.mana.utils.ManaGenerationHelper;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.resources.ResourceLocation;

public class Manas {
    public static final Mana EmptyMana = new Mana(new ManaProperties.Builder().canBeGenerated(false).build());

    public static final ResourceLocation FireManaIcon = ResourceLocation.fromNamespaceAndPath(
            ManaweaveAndRunes.MODID, "container/mana/fire_mana");
    public static final Mana FireMana = new Mana(
            new ManaProperties.Builder().color(0xFF0000)
                    .addGenerationCondition(
                            ManaGenerationHelper.GenerationCondition.SURROUNDED_BY_LAVA)
                    .canBeGenerated(true)
                    .icon(FireManaIcon)
                    .build());

    public static final ResourceLocation AirManaIcon = ResourceLocation.fromNamespaceAndPath(
            ManaweaveAndRunes.MODID, "container/mana/air_mana");
    public static final Mana AirMana = new Mana(
            new ManaProperties.Builder().color(0xADF3FF)
                    .addGenerationCondition(
                            ManaGenerationHelper.GenerationCondition.PLACED_HIGH)
                    .canBeGenerated(true)
                    .icon(AirManaIcon)
                    .build());
}
