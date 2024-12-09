package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.items.AbstractRuneItem;
import io.github.sfseeger.lib.mana.Mana;
import io.github.sfseeger.lib.mana.capability.IManaHandler;
import io.github.sfseeger.lib.mana.capability.ManaCapableBlockEntity;
import io.github.sfseeger.lib.mana.capability.ManaHandler;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class ManaCollectorBlockEntity extends ManaCapableBlockEntity {
    public static final int CAPACITY = 5000;
    public static final int MANA_PER_SOURCE = 5;
    private static final int MAX_RECEIVE = 5000;
    private static final int MAX_EXTRACT = 5000;

    private boolean isCollecting = false;

    private ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public ManaCollectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(), pos, blockState);

        this.manaHandler = new ManaHandler(CAPACITY, MAX_RECEIVE, MAX_EXTRACT, null) {
            @Override
            public void onContentChanged() {
                setChanged();
            }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state,
            ManaCollectorBlockEntity blockEntity) {
        // TODO: Make this code changeable using plugins / datapacks
        if (level.getGameTime() % 20 != 0) return;
        IManaHandler manaHandler = blockEntity.getManaHandler(null);
        IItemHandler itemHandler = blockEntity.getItemHandler(null);
        Item item = itemHandler.getStackInSlot(0).getItem();
        if (item instanceof AbstractRuneItem runeItem) {
            Mana mamaType = runeItem.getManaType();
            int potentialMana =
                    mamaType.canGenerateMana(level, pos, state) * MANA_PER_SOURCE * mamaType.getGenerationMultiplier();
            blockEntity.setCollecting(potentialMana > 0);
            manaHandler.receiveMana(potentialMana, mamaType, false);
        }
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return inventory;
    }

    public boolean isCollecting() {
        return isCollecting;
    }

    public void setCollecting(boolean collecting) {
        isCollecting = collecting;
    }
}
