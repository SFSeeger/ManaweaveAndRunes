package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.datamaps.ManaMapData;
import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaHandler;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNode;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNodeType;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ManaGeneratorBlock;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ManaGeneratorBlockEntity extends BlockEntity implements IManaNetworkSubscriber {
    public static final int CAPACITY = 1000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;

    private final ManaHandler manaHandler;
    private final int maxCookTime = 30;
    private ManaNetworkNode manaNetworkNode = new ManaNetworkNode(this, ManaNetworkNodeType.PROVIDER);
    private int maxBurnTime = 0;
    private int burnTimeRemaining = 0;
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(2) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case 0 -> ManaGeneratorBlockEntity.getManaMapData(stack).isPresent();
                case 1 -> stack.getBurnTime(null) > 0;
                default -> false;
            };
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            startCooking(getStackInSlot(0), getStackInSlot(1));
            ManaGeneratorBlockEntity.this.setChanged();
        }
    };
    private int cookTimeRemaining = 0;

    public ManaGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(MRBlockEntityInit.MANA_GENERATOR_BLOCK_ENTITY.get(), pos, blockState);
        manaHandler =
                new ManaHandler(CAPACITY, MAX_EXTRACT, MAX_RECEIVE, null) {
                    @Override
                    public void onContentChanged() {
                        markUpdated();
                    }
                };
    }

    @SuppressWarnings("unchecked")
    @Override
    public IManaHandler getManaHandler(@Nullable Direction side) {
        return this.manaHandler;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ManaGeneratorBlockEntity blockEntity) {
        blockEntity.burn(level, pos, state);
    }

    public static Optional<ManaMapData> getManaMapData(ItemStack stack) {
        return Optional.ofNullable(stack.getItemHolder().getData(ManaMapData.MANA_MAP_DATA));
    }

    public ItemStackHandler getItemHandler(@Nullable Direction side) {
        return itemStackHandler;
    }

    public void burn(Level level, BlockPos pos, BlockState state) {
        boolean flag = false;
        if (burnTimeRemaining > 0) {
            burnTimeRemaining--;
            if (!itemStackHandler.getStackInSlot(0).isEmpty()) {
                if (cookTimeRemaining < maxCookTime) {
                    cookTimeRemaining++;
                } else {
                    cookTimeRemaining = 0;
                    ItemStack stack = itemStackHandler.getStackInSlot(0);
                    ManaMapData data = getManaMapData(stack).orElse(null);
                    if (data != null) {
                        data.manaMap().forEach((mana, amount) -> manaHandler.receiveMana(amount, mana, false));
                        stack.shrink(1);
                    }
                }
            }
            flag = true;
        }
        if (burnTimeRemaining == 0 && !itemStackHandler.getStackInSlot(0).isEmpty() && !itemStackHandler.getStackInSlot(
                1).isEmpty()) {
            startCooking(itemStackHandler.getStackInSlot(0), itemStackHandler.getStackInSlot(1));
            flag = true;
        }

        if (!manaHandler.getManaTypesStored().isEmpty()) {
            manaHandler.getManaTypesStored()
                    .forEach(mana -> manaNetworkNode.provideMana(Math.min(manaHandler.getManaStored(mana), MAX_EXTRACT),
                                                                 mana));
        }

        level.setBlock(pos, state.setValue(ManaGeneratorBlock.LIT, isLit()), 3);
        if (flag) {
            markUpdated();
        }
    }

    public boolean isLit() {
        return burnTimeRemaining > 0;
    }

    public void invalidateCapability() {
        if (level != null) {
            level.invalidateCapabilities(worldPosition);
        }
    }

    public int getBurnTimeRemaining() {
        return burnTimeRemaining;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    public int getCookTime() {
        return cookTimeRemaining;
    }

    public int getMaxCookTime() {
        return maxCookTime;
    }

    public void startCooking(ItemStack resource, ItemStack fuel) {
        if (fuel.getBurnTime(null) > 0) {
            if (!resource.isEmpty() && getManaMapData(resource).isPresent()) {
                if (burnTimeRemaining == 0) {
                    burnTimeRemaining = maxBurnTime = fuel.getBurnTime(null);
                    fuel.shrink(1);
                }
            } else {
                cookTimeRemaining = 0;
            }
        }
    }


    @Override
    public ManaNetworkNode getManaNetworkNode() {
        return manaNetworkNode;
    }

    @Override
    public void setManaNetworkNode(ManaNetworkNode node) {
        manaNetworkNode = node;
    }

    @Override
    public void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            invalidateCapability();
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemStackHandler.deserializeNBT(registries, tag.getCompound("Inventory"));

        CompoundTag burnTag = tag.getCompound("Burn");
        burnTimeRemaining = burnTag.getInt("BurnTime");
        maxBurnTime = burnTag.getInt("MaxBurnTime");
        cookTimeRemaining = burnTag.getInt("CookTime");

        if (tag.contains("mana")) manaHandler.deserializeNBT(registries, tag.getCompound("mana"));
        manaNetworkNode = ManaNetworkNode.deserializeNBT(tag.getCompound("mana_network_node"), registries, this)
                .orElse(new ManaNetworkNode(this, ManaNetworkNodeType.PROVIDER));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", itemStackHandler.serializeNBT(registries));

        CompoundTag burnTag = new CompoundTag();
        burnTag.putInt("BurnTime", burnTimeRemaining);
        burnTag.putInt("MaxBurnTime", maxBurnTime);
        burnTag.putInt("CookTime", cookTimeRemaining);
        tag.put("Burn", burnTag);

        tag.put("Mana", manaHandler.serializeNBT(registries));
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
            manaNetworkNode.updateNetwork();
            manaNetworkNode.connectPendingNodes();
        }
    }
}
