package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.mana.capability.IManaHandler;
import io.github.sfseeger.lib.mana.capability.SingleManaHandler;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
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
        manaHandler =
                new SingleManaHandler(ManaInit.FIRE_MANA.get(), CAPACITY, MAX_RECEIVE, MAX_EXTRACT) {
                    @Override
                    public void onContentChanged() {
                        setChanged();
                    }
                };
    }

    public IManaHandler getManaHandler(@Nullable Direction side) {
        return manaHandler;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        manaHandler.deserializeNBT(registries, tag.getCompound("manaHandler"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("manaHandler", manaHandler.serializeNBT(registries));
    }
}
