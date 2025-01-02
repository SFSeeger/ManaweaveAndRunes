package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.IManaHandler;
import io.github.sfseeger.lib.common.mana.capability.ManaweaveAndRunesCapabilities;
import io.github.sfseeger.lib.common.rituals.IRitualManager;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualDataCapable;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorType;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorTypes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRParticleTypeInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRTagInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.ManaweaveAndRunesBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class RitualAnchorBlockEntity extends BlockEntity implements IRitualManager {
    public static final Ritual.RitualOriginType ORIGIN_TYPE = Ritual.RitualOriginType.ANCHOR;
    public static final Predicate<BlockState> MANA_CAPABLE_BLOCK =
            blockState -> blockState.is(MRTagInit.RITUAL_MANA_PROVIDERS);
    RitualState currentRitualState = RitualState.IDLE;
    Ritual currentRitual;
    int ritualTicks = 0;
    boolean isActive = false;
    List<BlockPos> pedestalPositions = new ArrayList<>();
    List<BlockPos> pedestalsToVisit = new ArrayList<>();
    List<Ingredient> requiredItems = new ArrayList<>();
    List<ItemStack> consumedItems = new ArrayList<>();
    RitualContext ritualContext = new RitualContext();
    private List<BlockPos> manaSources = new ArrayList<>();

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
        RandomSource randomsource = level.random;
        RitualState currentState = blockEntity.getState();
        if (!(currentState == RitualState.IDLE)) {
            if (level.getGameTime() % 50 == 0) {
                blockEntity.pedestalPositions = blockEntity.getRitualAnchorType()
                        .findBlocks(level, ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get());
            }
            switch (currentState) {
                case INITIAL_ITEM_CONSUME -> {
                    List<BlockPos> filteredPedestalPositions = blockEntity.pedestalPositions.stream()
                            .filter(pedestalPos -> level.getBlockEntity(
                                    pos.offset(pedestalPos)) instanceof RunePedestalBlockEntity re && !re.getItem()
                                    .isEmpty())
                            .toList();

                    for (BlockPos pedestalPos : filteredPedestalPositions.stream().map(pos::offset).toList()) {
                        Vec3 pedestalVec =
                                new Vec3(pedestalPos.getX(), pedestalPos.getY(), pedestalPos.getZ());
                        Vec3 vecToConcentrator = new Vec3(pos.getX(), pos.getY(), pos.getZ()).vectorTo(pedestalVec);
                        for (int i = 0; i < 4; i++) {
                            Vec3 randomPedestalVec = vecToConcentrator.offsetRandom(randomsource, .5f);
                            level.addParticle(MRParticleTypeInit.MANA_PARTICLE.get(),
                                              pos.getX() + 0.5f, pos.getY() + 1.5f, pos.getZ() + 0.5f,
                                              randomPedestalVec.x(), randomPedestalVec.y(), randomPedestalVec.z());
                        }
                    }
                }
                case TICK -> {
                    Ritual ritual = blockEntity.getRitual();
                    if (ritual != null) {
                        ritual.onRitualClientTick(level, pos, state, blockEntity.ritualTicks, blockEntity.ritualContext,
                                                  ORIGIN_TYPE);
                    }
                }
            }
        }
    }

    public static void ritualTick(Level level, BlockPos pos, BlockState state, RitualAnchorBlockEntity blockEntity) {
        RitualState currentState = blockEntity.getState();
        if (currentState == null || currentState == RitualState.IDLE) {
            return;
        }

        blockEntity.executeStepAndTransition(level, pos, state, blockEntity.ritualTicks, blockEntity.ritualContext,
                                             ORIGIN_TYPE);

        switch (blockEntity.getState()) {
            case TICK -> {
                if (blockEntity.getRitual().getDuration() != -1) {
                    blockEntity.ritualTicks++;
                    if (blockEntity.ritualTicks >= blockEntity.getRitual()
                            .getDuration()) {
                        blockEntity.transition(RitualStepResult.END);
                    }
                }
            }
            case FINISH, ABORT -> {
                blockEntity.executeStep(level, pos, state, blockEntity.ritualTicks, blockEntity.ritualContext,
                                        ORIGIN_TYPE);
                blockEntity.ritualTicks = 0;
                blockEntity.pedestalPositions.clear();
                blockEntity.pedestalsToVisit.clear();
                blockEntity.requiredItems = List.of();
                blockEntity.consumedItems.clear();
                blockEntity.ritualContext = new RitualContext();
                blockEntity.transition(RitualStepResult.END);
            }
            default -> {
            }
        }
    }

    @Override
    public void startRitual(Ritual ritual) {
        ritualTicks = 0;
        IRitualManager.super.startRitual(ritual);
    }

    @Override
    public RitualStepResult consumeInitialItem(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context, Ritual.RitualOriginType originType) {
        if (level.getGameTime() % 20 != 0) {
            return RitualStepResult.SUCCESS;
        }
        if (pedestalsToVisit.isEmpty()) {
            if (requiredItems.stream().allMatch(ing -> consumedItems.stream().anyMatch(ing::test))) {
                return RitualStepResult.SKIP;
            }
            return RitualStepResult.END;
        }

        Iterator<BlockPos> iterator = pedestalsToVisit.iterator();

        while (iterator.hasNext()) {
            BlockPos offset = iterator.next();
            BlockPos worldPos = pos.offset(offset);
            BlockEntity blockEntity = level.getBlockEntity(worldPos);
            if (blockEntity instanceof RunePedestalBlockEntity pBE) {
                boolean flag = false;
                if (getRitual().getInitialItemCost(level).stream().anyMatch(el -> el.test(pBE.getItem()))) {
                    consumedItems.add(pBE.getItemHandler(null).extractItem(0, 1, false));
                    flag = true;
                }
                iterator.remove();
                pedestalsToVisit.remove(offset);
                if (flag) {
                    return RitualStepResult.SUCCESS;
                }
            }
        }
        return RitualStepResult.SUCCESS;
    }

    @Override
    public RitualStepResult consumeTickItem(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context,
            Ritual.RitualOriginType originType) {
        //TODO: Implement
        return RitualStepResult.SUCCESS;
    }

    @Override
    public RitualStepResult consumeMana(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context,
            Ritual.RitualOriginType originType) {
        if (getRitual() != null && ticksPassed % getRitual().getManaRate() == 0) {
            Map<Mana, Integer> manaConsumed = new HashMap<>();
            Map<Mana, Integer> manaToConsume = getRitual().getManaCost(level);

            for (BlockPos manaSource : manaSources) {
                IManaHandler handler =
                        level.getCapability(ManaweaveAndRunesCapabilities.MANA_HANDLER_BLOCK, manaSource, null);
                if (handler != null) {
                    for (Mana mana : manaToConsume.keySet()) {
                        int amount = manaToConsume.get(mana) - manaConsumed.getOrDefault(mana, 0);
                        int extracted = handler.extractMana(amount, mana, false);
                        manaConsumed.put(mana, manaConsumed.getOrDefault(mana, 0) + extracted);
                    }
                }
            }
            if (manaConsumed.equals(manaToConsume)) {
                return RitualStepResult.SUCCESS;
            } else {
                return RitualStepResult.ABORT;
            }
        }
        return RitualStepResult.SKIP;
    }

    @Override
    public void markUpdated() {
        setChanged();
        if (level != null)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), RitualAnchorBlock.UPDATE_ALL);
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
        //TODO: Check if all required extra data is provided
        if (getState() != RitualState.IDLE || !stack.is(Items.CARROT_ON_A_STICK)) {
            return false;
        }
        List<ItemStack> items = new ArrayList<>();
        RitualContext ritualContext = new RitualContext();
        for (BlockPos offset : getRitualAnchorType().findBlocks(level,
                                                                ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get())) {
            BlockPos worldPos = getBlockPos().offset(offset);
            BlockEntity blockEntity = level.getBlockEntity(worldPos);
            if (blockEntity instanceof RunePedestalBlockEntity pBE) {
                ItemStack itemStack = pBE.getItem();
                if (!itemStack.isEmpty()) {
                    if (itemStack.getItem() instanceof IRitualDataCapable ritItem && ritItem.getData(
                            itemStack) != null) {
                        ritualContext.putData(null, ritItem.getData(itemStack));
                    }
                    items.add(itemStack);
                }
            }
        }
        Ritual ritual = getMatchingRitual(items, getRitualAnchorType().getTier(), ORIGIN_TYPE,
                                          level).orElse(null);
        if (ritual == null) {
            return false;
        }
        ritualContext.putData("starting_player", new PlayerRitualData(player));

        this.pedestalPositions =
                getRitualAnchorType().findBlocks(level, ManaweaveAndRunesBlockInit.RUNE_PEDESTAL_BLOCK.get());
        this.pedestalsToVisit = new ArrayList<>(pedestalPositions);
        this.requiredItems = ritual.getInitialItemCost(level);
        this.ritualContext = ritualContext;
        this.manaSources = findManaSources();

        startRitual(ritual);
        return true;
    }

    public RitualAnchorType getRitualAnchorType() {
        return getBlockState().getBlock() instanceof RitualAnchorBlock block ? block.ritualAnchorType : RitualAnchorTypes.NOVICE;
    }

    public List<BlockPos> findManaSources() {
        BiPredicate<BlockState, BlockPos> IS_AVAILABLE_MANA_CAPABLE_BLOCK =
                (blockState, pos) -> MANA_CAPABLE_BLOCK.test(blockState) && worldPosition.distManhattan(pos) <= 20;
        List<BlockPos> positions = new ArrayList<>();

        if (level == null) return positions;

        int chunkX = worldPosition.getX() >> 4;
        int chunkZ = worldPosition.getZ() >> 4;
        for (int i = chunkX - 1; i <= chunkX + 1; i++) {
            for (int j = chunkZ - 1; j <= chunkZ + 1; j++) {
                level.getChunk(i, j)
                        .findBlocks(MANA_CAPABLE_BLOCK, IS_AVAILABLE_MANA_CAPABLE_BLOCK,
                                    (pos, blockState) -> positions.add(pos));
            }
        }
        return positions;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        deserializeNBT(tag.getCompound("current_ritual"), registries);
        ritualTicks = tag.getInt("ritual_ticks");
        setActive(tag.getBoolean("is_active"));
        if (tag.contains("context")) ritualContext = RitualContext.deserializeNBT(tag.getCompound("context"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("current_ritual", currentRitual != null ? serializeNBT(registries) : new CompoundTag());
        tag.putInt("ritual_ticks", ritualTicks);
        tag.putBoolean("is_active", isActive());
        tag.put("context", ritualContext.serializeNBT());
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
