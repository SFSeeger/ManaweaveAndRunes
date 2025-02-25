package io.github.sfseeger.manaweave_and_runes.common.blocks;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class ManaInfusedRock extends Block {
    public ManaInfusedRock() {
        super(Properties.of().strength(1.5F)
                      .lightLevel(
                              (s) -> 3)
                      .requiresCorrectToolForDrops()
                      .sound(SoundType.STONE)
                      .mapColor(DyeColor.GRAY));
    }
}
