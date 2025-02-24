package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.IManaItem;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNode;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNodeType;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ManaCollectorBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.util.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RunePedestalBlockEntity extends BlockEntity implements IManaNetworkSubscriber {
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            RunePedestalBlockEntity.this.markChanged();
        }
    };
    public ManaNetworkNode node = new ManaNetworkNode(this, ManaNetworkNodeType.HYBRID, 2, false);

    public RunePedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(MRBlockEntityInit.RUNE_PEDESTAL_BLOCK_ENTITY.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RunePedestalBlockEntity blockEntity) {
        if (level.getGameTime() % 40 != 0) return;
        ManaNetworkNodeType type = blockEntity.node.getNodeType();
        if (type == ManaNetworkNodeType.HYBRID) return;
        IManaHandler handler = blockEntity.getManaHandler(null);
        if (handler == null) return;

        switch (type) {
            case PROVIDER -> {
                List<Mana> manaTypesStored = handler.getManaTypesStored();
                if (manaTypesStored.isEmpty()) return;
                Mana mana = manaTypesStored.getFirst();
                int stored = handler.getManaStored(mana);
                int maxExtract = handler.extractMana(stored, mana, true);
                blockEntity.node.provideMana(maxExtract, mana);
            }
            case RECEIVER -> {
                ItemStack stack = blockEntity.getItemHandler(null).getStackInSlot(0);
                if (stack.getItem() instanceof IManaItem manaItem) {
                    List<Mana> manas = manaItem.getManaTypes(stack);
                    if (manas.isEmpty()) return;

                    for (Mana mana : manas) {
                        int maxReceive = handler.receiveMana(handler.getManaCapacity(), mana, true);
                        blockEntity.node.requestMana(maxReceive, mana);
                    }
                }
            }
        }
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, RunePedestalBlockEntity blockEntity) {
        ManaNetworkNode node = blockEntity.getManaNetworkNode();
        ManaNetworkNodeType type = node.getNodeType();
        if (!(blockEntity.getItem().getItem() instanceof IManaItem)) return;
        boolean particleUp = type == ManaNetworkNodeType.RECEIVER || type == ManaNetworkNodeType.HYBRID;
        boolean particleDown = type == ManaNetworkNodeType.PROVIDER || type == ManaNetworkNodeType.HYBRID;
        Vec3 vec = ParticleUtils.randomPosInsideBox(blockPos, level.random, 0.3, 1.1, 0.3, 0.6, 1.3, 0.6);
        Vec3 vec1 = ParticleUtils.randomPosInsideBox(blockPos, level.random, 0.3, 1.1, 0.3, 0.6, 1.3, 0.6);

        //TODO: Replace with custom particles
        if (particleDown)
            level.addParticle(ParticleTypes.ASH, vec.x, vec.y, vec.z, 0, -0.2, 0);
        if (particleUp)
            level.addParticle(ParticleTypes.GLOW, vec1.x, vec1.y, vec1.z, 0, 0.2, 0);
    }

    public void markChanged() {
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(),
                    ManaCollectorBlock.UPDATE_ALL);
            level.invalidateCapabilities(worldPosition);
        }
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return inventory;
    }

    @SuppressWarnings("unchecked")
    public IManaHandler getManaHandler(@Nullable Direction side) {
        ItemStack stack = inventory.getStackInSlot(0);
        return stack.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_ITEM);
    }

    @Override
    public ManaNetworkNode getManaNetworkNode() {
        return node;
    }

    @Override
    public void setManaNetworkNode(ManaNetworkNode node) {
        this.node = node;
    }

    @Override
    public void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), ManaCollectorBlock.UPDATE_ALL);
        }
    }

    public boolean placeItem(ItemStack stack) {
        if (inventory.getStackInSlot(0).isEmpty()) {
            inventory.setStackInSlot(0, stack.copy());
            stack.setCount(0);
            this.markChanged();
            return true;
        }
        return false;
    }

    public ItemStack removeItem() {
        ItemStack stack = inventory.getStackInSlot(0);
        inventory.setStackInSlot(0, ItemStack.EMPTY);
        this.markChanged();
        return stack;
    }

    public ItemStack getItem() {
        return inventory.getStackInSlot(0);
    }

    public void setItem(ItemStack item) {
        inventory.setStackInSlot(0, item);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        node = ManaNetworkNode.deserializeNBT(tag.getCompound("mana_network_node"), registries, this).orElse(
                new ManaNetworkNode(this, ManaNetworkNodeType.HYBRID, 2, false)
        );
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.put("mana_network_node", node.serializeNBT(registries));
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
        if (node != null) {
            node.updateNetwork();
            node.connectPendingNodes();
        }
    }

    public boolean toggleState() {
        ManaNetworkNodeType type = node.getNodeType();
        ManaNetworkNodeType newType;
        switch (type) {
            case PROVIDER -> newType = ManaNetworkNodeType.RECEIVER;
            case RECEIVER -> newType = ManaNetworkNodeType.HYBRID;
            case HYBRID -> newType = ManaNetworkNodeType.PROVIDER;
            default -> newType = ManaNetworkNodeType.HYBRID;
        }
        markUpdated();
        return node.setNodeType(newType);
    }
}
