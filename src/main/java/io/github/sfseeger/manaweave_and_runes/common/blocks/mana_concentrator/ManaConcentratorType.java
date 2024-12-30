package io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator;

import io.github.sfseeger.lib.common.AbstractMultiBlockType;
import io.github.sfseeger.lib.common.Tier;
import net.minecraft.resources.ResourceLocation;

public class ManaConcentratorType extends AbstractMultiBlockType {
    public ManaConcentratorType(ResourceLocation location, Tier tier) {
        super(tier, location);
    }
}
