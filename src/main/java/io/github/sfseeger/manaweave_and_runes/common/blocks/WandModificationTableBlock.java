package io.github.sfseeger.manaweave_and_runes.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class WandModificationTableBlock extends Block {
    public WandModificationTableBlock() {
        super(Properties.of().sound(SoundType.ANVIL).requiresCorrectToolForDrops());
    }

}
