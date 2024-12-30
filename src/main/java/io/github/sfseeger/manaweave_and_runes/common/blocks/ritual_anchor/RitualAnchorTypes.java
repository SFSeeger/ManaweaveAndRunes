package io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.resources.ResourceLocation;

public class RitualAnchorTypes {
    public static final RitualAnchorType NOVICE =
            new RitualAnchorType(Tier.NOVICE, ResourceLocation.fromNamespaceAndPath(
                    ManaweaveAndRunes.MODID, "novice_ritual_anchor"));
    public static final RitualAnchorType MASTER =
            new RitualAnchorType(Tier.MASTER, ResourceLocation.fromNamespaceAndPath(
                    ManaweaveAndRunes.MODID, "master_ritual_anchor"));
    public static final RitualAnchorType ASCENDED =
            new RitualAnchorType(Tier.ASCENDED, ResourceLocation.fromNamespaceAndPath(
                    ManaweaveAndRunes.MODID, "ascended_ritual_anchor"));
}
