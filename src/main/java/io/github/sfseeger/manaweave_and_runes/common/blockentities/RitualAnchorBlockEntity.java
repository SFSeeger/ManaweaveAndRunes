package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.rituals.IRitualManager;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorType;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorTypes;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RitualAnchorBlockEntity extends BlockEntity implements IRitualManager {
    public static final Ritual.RitualOriginType ORIGIN_TYPE = Ritual.RitualOriginType.ANCHOR;
    RitualState currentRitualState = RitualState.IDLE;
    Ritual currentRitual;
    int ritualTicks = 0;
    boolean isActive = false;


    public RitualAnchorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ManaweaveAndRunesBlockEntityInit.RITUAL_ANCHOR_BLOCK_ENTITY.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RitualAnchorBlockEntity blockEntity) {
        if (level.getGameTime() % 20 == 0) {
            blockEntity.setActive(blockEntity.getRitualAnchorType().isValidShape(level, pos));
        }

        ritualTick(level, pos, state, blockEntity);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, RitualAnchorBlockEntity blockEntity) {
        if (blockEntity.getState() == RitualState.TICK) {
            blockEntity.getRitual().onRitualClientTick(level, pos, state, blockEntity.ritualTicks, ORIGIN_TYPE);
        }
    }

    public static void ritualTick(Level level, BlockPos pos, BlockState state, RitualAnchorBlockEntity blockEntity) {
        RitualState currentState = blockEntity.getState();
        if (currentState == null || currentState == RitualState.IDLE) {
            return;
        }

        blockEntity.executeStepAndTransition(level, pos, state, blockEntity.ritualTicks, ORIGIN_TYPE);

        switch (blockEntity.getState()) {
            case FINISH, ABORT -> {
                blockEntity.ritualTicks = 0;
            }
            case TICK -> {
                blockEntity.ritualTicks++;
                if (blockEntity.getRitual().getDuration() != -1 && blockEntity.ritualTicks >= blockEntity.getRitual()
                        .getDuration()) {
                    blockEntity.transition(RitualStepResult.END);
                }
            }
            case MANA_CONSUME, TICK_ITEM_CONSUME -> ritualTick(level, pos, state, blockEntity);
        }
    }

    @Override
    public void startRitual(Ritual ritual) {
        ritualTicks = 0;
        IRitualManager.super.startRitual(ritual);
    }

    @Override
    public RitualStepResult consumeInitialItem(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            Ritual.RitualOriginType originType) {
        if (level.getGameTime() % 20 != 0) {
            return RitualStepResult.SUCCESS;
        }

        for (BlockPos offset : getRitualAnchorType().findBlocks(level,
                                                                ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get())) {
            BlockPos worldPos = pos.offset(offset);
            BlockEntity blockEntity = level.getBlockEntity(worldPos);
            if (blockEntity instanceof RunePedestalBlockEntity pBE) {
                if (getRitual().getInitialItemCost(level).stream().anyMatch(el -> el.test(pBE.getItem()))) {
                    pBE.getItemHandler(null).extractItem(0, 1, false);
                    return RitualStepResult.SUCCESS;
                }
            }
        }
        return RitualStepResult.SKIP;
    }

    @Override
    public RitualStepResult consumeTickItem(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            Ritual.RitualOriginType originType) {
        //TODO: Implement
        return RitualStepResult.SUCCESS;
    }

    @Override
    public RitualStepResult consumeMana(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            Ritual.RitualOriginType originType) {
        //TODO: Implement
        return RitualStepResult.SKIP;
    }

    @Override
    public Ritual getRitual() {
        return currentRitual;
    }

    @Override
    public void setRitual(Ritual ritual) {
        this.currentRitual = ritual;
    }

    @Override
    public RitualState getState() {
        return currentRitualState;
    }

    @Override
    public void setState(RitualState state) {
        this.currentRitualState = state;
    }

    public boolean checkAndStartRitual(Level level, Player player, ItemStack stack) {
        //TODO: Check if player knows this ritual
        if (getState() != RitualState.IDLE || !stack.is(Items.CARROT_ON_A_STICK)) {
            return false;
        }
        List<ItemStack> items = new ArrayList<>();
        for (BlockPos offset : getRitualAnchorType().findBlocks(level,
                                                                ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get())) {
            BlockPos worldPos = getBlockPos().offset(offset);
            BlockEntity blockEntity = level.getBlockEntity(worldPos);
            if (blockEntity instanceof RunePedestalBlockEntity pBE) {
                ItemStack item = pBE.getItem();
                if (!item.isEmpty()) items.add(item);
            }
        }
        Ritual ritual = getMatchingRitual(items, getRitualAnchorType().getTier(), ORIGIN_TYPE,
                                          level).orElse(null);
        if (ritual == null) {
            return false;
        }
        startRitual(ritual);
        return true;
    }

    public RitualAnchorType getRitualAnchorType() {
        return getBlockState().getBlock() instanceof RitualAnchorBlock block ? block.ritualAnchorType : RitualAnchorTypes.NOVICE;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        deserializeNBT(tag.getCompound("current_ritual"), registries);
        ritualTicks = tag.getInt("ritual_ticks");
        setActive(tag.getBoolean("is_active"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("current_ritual", currentRitual != null ? serializeNBT(registries) : new CompoundTag());
        tag.putInt("ritual_ticks", ritualTicks);
        tag.putBoolean("is_active", isActive());
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
