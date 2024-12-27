package io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.resources.ResourceLocation;

public class ManaConcentratorTypes {
    public static final ManaConcentratorType NOVICE = new ManaConcentratorType(
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "novice_mana_concentrator"), Tier.NOVICE);
    public static final ManaConcentratorType MASTER = new ManaConcentratorType(
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "master_mana_concentrator"), Tier.MASTER);
    public static final ManaConcentratorType ASCENDANT = new ManaConcentratorType(
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "ascendant_mana_concentrator"),
            Tier.ASCENDED);
}
