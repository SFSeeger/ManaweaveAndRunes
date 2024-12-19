package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.mana.capability.ManaHandler;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ManaStorageBlockEntity extends BlockEntity {
    public static final int MANA_SLOTS = 5;
    ManaHandler manaHandler;

    public ManaStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(), pos, blockState);
        manaHandler = new ManaHandler(1000, 1000, 1000, MANA_SLOTS, null) {
            @Override
            public void onContentChanged() {
                setChanged();
            }
        };
    }

    public ManaHandler getManaHandler(Direction side) {
        return this.manaHandler;
    }
}
