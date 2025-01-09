package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.items.AbstractRuneItem;
import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.lib.common.mana.capability.SingleManaHandler;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNode;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNodeType;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ManaCollectorBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class ManaCollectorBlockEntity extends BlockEntity implements IManaNetworkSubscriber {
    public static final int CAPACITY = 5000;
    public static final int MANA_PER_SOURCE = 5;
    private static final int MAX_RECEIVE = 5000;
    private static final int MAX_EXTRACT = 5000;
    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof AbstractRuneItem;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markUpdated();
        }
    };
    private final SingleManaHandler manaHandler;
    ManaNetworkNode manaNetworkNode = new ManaNetworkNode(this, ManaNetworkNodeType.PROVIDER);
    private boolean isCollecting = false;

    public ManaCollectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.MANA_COLLECTOR_BLOCK_ENTITY.get(), pos, blockState);

        this.manaHandler = new SingleManaHandler(CAPACITY, MAX_RECEIVE, MAX_EXTRACT) {
            @Override
            public void onContentChanged() {
                super.onContentChanged();
                markUpdated();
            }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state,
            ManaCollectorBlockEntity blockEntity) {
        if (level.getGameTime() % 20 != 0) return;
        IItemHandler itemHandler = blockEntity.getItemHandler(null);
        ItemStack stack = itemHandler.getStackInSlot(0);
        Item item = stack.getItem();
        if (!stack.isEmpty() && item instanceof AbstractRuneItem runeItem) {
            Mana mamaType = runeItem.getManaType();
            int potentialMana =
                    mamaType.canGenerateMana(level, pos, state) * MANA_PER_SOURCE * mamaType.getGenerationMultiplier();
            blockEntity.setCollecting(potentialMana > 0);
            IManaHandler stackManaHandler = stack.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
            if (stackManaHandler != null) {
                stackManaHandler.receiveMana(potentialMana, mamaType, false);
            }
            blockEntity.getManaNetworkNode().provideMana(potentialMana, mamaType);
            blockEntity.markUpdated();
        }
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return inventory;
    }

    public IManaHandler getManaHandler(@Nullable Direction side) {
        return inventory.getStackInSlot(0).getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
    }

    @Override
    public ManaNetworkNode getManaNetworkNode() {
        return manaNetworkNode;
    }

    @Override
    public void setManaNetworkNode(ManaNetworkNode node) {
        this.manaNetworkNode = node;
    }

    public void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(),
                                   ManaCollectorBlock.UPDATE_ALL);
            level.invalidateCapabilities(getBlockPos());
        }
    }

    public boolean placeRune(@Nullable LivingEntity entity, ItemStack rune) {
        if (inventory.getStackInSlot(0).isEmpty() && rune.getItem() instanceof AbstractRuneItem) {
            inventory.setStackInSlot(0, rune.copy());
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(),
                                 GameEvent.Context.of(entity, this.getBlockState()));
            markUpdated();
            return true;
        }
        return false;
    }

    public ItemStack removeRune() {
        ItemStack stack = inventory.extractItem(0, 1, false);
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(),
                                   ManaCollectorBlock.UPDATE_ALL);
        }
        return stack;
    }

    public boolean isCollecting() {
        return isCollecting;
    }

    public void setCollecting(boolean collecting) {
        isCollecting = collecting;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        manaNetworkNode = ManaNetworkNode.deserializeNBT(tag.getCompound("mana_network_node"), registries, this)
                .orElse(new ManaNetworkNode(this, ManaNetworkNodeType.PROVIDER));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.put("mana_network_node",
                manaNetworkNode != null ? manaNetworkNode.serializeNBT(registries) : new CompoundTag());
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (manaNetworkNode != null) {
            manaNetworkNode.connectPendingNodes();
            manaNetworkNode.updateNetwork();
        }
    }
}
