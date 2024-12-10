package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.mana.capability.ManaCapableBlockEntity;
import io.github.sfseeger.lib.mana.capability.ManaHandler;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ManaStorageBlockEntity extends ManaCapableBlockEntity {
    public static final int CAPACITY = 1000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;

    public ManaStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(), pos, blockState);
        manaHandler =
                new ManaHandler(CAPACITY, MAX_EXTRACT, MAX_RECEIVE, null) {
                    @Override
                    public void onContentChanged() {
                        setChanged();
                    }
                };
    }
}
