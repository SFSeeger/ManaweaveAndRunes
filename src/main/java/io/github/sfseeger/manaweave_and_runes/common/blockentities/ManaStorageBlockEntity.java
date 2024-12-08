package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.manaweave_and_runes.common.api.capability.IManaHandler;
import io.github.sfseeger.manaweave_and_runes.common.api.capability.SingleManaHandler;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesManaInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ManaStorageBlockEntity extends BlockEntity {
    public static final int CAPACITY = 1000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;

    private SingleManaHandler manaHandler;

    public ManaStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_STORAGE_BLOCK_ENTITY.get(), pos, blockState);
        manaHandler = new SingleManaHandler(ManaweaveAndRunesManaInit.FIRE_MANA.get(), CAPACITY, MAX_RECEIVE,
                                            MAX_EXTRACT);
    }

    public IManaHandler getManaHandler(@Nullable Direction side) {
        return manaHandler;
    }
}
