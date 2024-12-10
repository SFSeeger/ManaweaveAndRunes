package io.github.sfseeger.lib.mana;

import io.github.sfseeger.lib.mana.utils.ManaGenerationHelper;

public class Manas {
    public static final Mana EmptyMana = new Mana(new ManaProperties.Builder().canBeGenerated(false).build());
    public static final Mana FireMana = new Mana(
            new ManaProperties.Builder().color(0xFF0000).addGenerationCondition(
                    ManaGenerationHelper.GenerationCondition.SURROUNDED_BY_LAVA).canBeGenerated(true).build());
    public static final Mana AirMana = new Mana(
            new ManaProperties.Builder().color(0xADF3FF).addGenerationCondition(
                    ManaGenerationHelper.GenerationCondition.PLACED_HIGH).canBeGenerated(true).build());
}
