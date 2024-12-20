package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.blockentities.IManaCapable;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaHandler;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ManaStorageBlockEntity extends BlockEntity implements IManaCapable {
    public static final int CAPACITY = 1000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;

    private final ManaHandler manaHandler;

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

    @Override
    public IManaHandler getManaHandler(@Nullable Direction side) {
        return this.manaHandler;
    }
}
