package io.github.sfseeger.manaweave_and_runes.common.blockentities;

import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.mana.capability.ManaHandler;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNode;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNodeType;
import io.github.sfseeger.lib.common.rituals.IRitualManager;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.RitualUtils;
import io.github.sfseeger.lib.common.rituals.ritual_data.IRitualDataCapable;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorType;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ritual_anchor.RitualAnchorTypes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockEntityInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRBlockInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.init.MRParticleTypeInit;
import io.github.sfseeger.manaweave_and_runes.core.util.Utils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RitualAnchorBlockEntity extends BlockEntity implements IRitualManager, IManaNetworkSubscriber, GeoBlockEntity {
    public static final Ritual.RitualOriginType ORIGIN_TYPE = Ritual.RitualOriginType.ANCHOR;

    //ANIMATIONS
    protected static final RawAnimation DEPLOY_ANIMATION = RawAnimation.begin().thenLoop("idle_inactive");
    protected static final RawAnimation IDLE_ACTIVE = RawAnimation.begin().thenLoop("idle_active");
    protected static final RawAnimation ACTIVATION_ANIMATION =
            RawAnimation.begin().thenPlay("activate").thenLoop("idle_active");
    protected static final RawAnimation RUNNING_ANIMATION = RawAnimation.begin().thenLoop("ritual_active");
    protected static final RawAnimation DEACTIVATION_ANIMATION = RawAnimation.begin().thenPlay("deactivate");

    public final ManaHandler manaHandler = new ManaHandler(10_000, 10_000, 10_000, null);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private RitualState currentRitualState = RitualState.IDLE;
    int ritualTicks = 0;
    boolean isActive = false;
    private Ritual currentRitual;
    private List<BlockPos> pedestalPositions = new ArrayList<>();
    private List<BlockPos> pedestalsToVisit = new ArrayList<>();
    private List<Ingredient> requiredItems = new ArrayList<>();
    private List<ItemStack> consumedItems = new ArrayList<>();
    private RitualContext ritualContext = new RitualContext();
    private ManaNetworkNode manaNetworkNode = new ManaNetworkNode(this, ManaNetworkNodeType.RECEIVER, 20, true);

    public RitualAnchorBlockEntity(BlockPos pos, BlockState blockState) {
        super(MRBlockEntityInit.RITUAL_ANCHOR_BLOCK_ENTITY.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RitualAnchorBlockEntity blockEntity) {
        if (level.getGameTime() % 20 == 0) {
            boolean oldState = blockEntity.isActive();
            blockEntity.setActive(blockEntity.getRitualAnchorType().isValidShape(level, pos));

            if (oldState != blockEntity.isActive()) {
                if (blockEntity.isActive()) {
                    blockEntity.triggerAnim("controller", "activate");
                } else {
                    blockEntity.triggerAnim("controller", "deactivate");
                }
            }
        }

        ritualTick(level, pos, state, blockEntity);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, RitualAnchorBlockEntity blockEntity) {
        RandomSource randomsource = level.random;
        RitualState currentState = blockEntity.getState();

        if (!(currentState == RitualState.IDLE)) {
            if (level.getGameTime() % 50 == 0) {
                blockEntity.pedestalPositions = blockEntity.getRitualAnchorType()
                        .findBlocks(level, MRBlockInit.RUNE_PEDESTAL_BLOCK.get());
            }
            switch (currentState) {
                case INITIAL_ITEM_CONSUME -> {
                    List<BlockPos> filteredPedestalPositions = blockEntity.pedestalPositions.stream()
                            .filter(pedestalPos -> level.getBlockEntity(
                                    pos.offset(pedestalPos)) instanceof RunePedestalBlockEntity re && !re.getItem()
                                    .isEmpty())
                            .toList();

                    for (BlockPos pedestalPos : filteredPedestalPositions.stream().map(pos::offset).toList()) {
                        Vec3 pedestalVec = new Vec3(pedestalPos.getX(), pedestalPos.getY(), pedestalPos.getZ());
                        Vec3 vecToConcentrator = new Vec3(pos.getX(), pos.getY(), pos.getZ()).vectorTo(pedestalVec);
                        for (int i = 0; i < 4; i++) {
                            Vec3 randomPedestalVec = vecToConcentrator.offsetRandom(randomsource, .5f);
                            level.addParticle(MRParticleTypeInit.MANA_TRAVEL_PARTICLE.get(), pos.getX() + 0.5f,
                                              pos.getY() + 1.5f, pos.getZ() + 0.5f, randomPedestalVec.x(),
                                              randomPedestalVec.y(), randomPedestalVec.z());
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

        blockEntity.triggerAnim("controller", "start_ritual");


        RitualState newState = blockEntity.executeStepAndTransition(level, pos, state, blockEntity.ritualTicks,
                                                                    blockEntity.ritualContext,
                                                                    ORIGIN_TYPE);
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
            if (Utils.compareIngredientsToItems(requiredItems, consumedItems)) {
                requestRequiredMana();
                requiredItems = List.of();
                consumedItems.clear();
                return RitualStepResult.SKIP;
            }
            RitualUtils.displayMessageToStartingPlayer(
                    Component.translatable("ritual.manaweave_and_runes.item_insufficient"),
                    level, context);
            return RitualStepResult.ABORT;
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
            RitualContext context, Ritual.RitualOriginType originType) {
        List<Ingredient> requiredItems = getRitual().getTickItemCost(level);

        // To ensure that mana rate can be != to item rate
        if (ticksPassed % getRitual().getManaRate(level) == 0) {
            requestRequiredMana();
        }

        if (ticksPassed % getRitual().getItemRate(level) != 0 || requiredItems.isEmpty()) {
            return RitualStepResult.SUCCESS;
        }

        List<ItemStack> consumedItems = new ArrayList<>();

        for (BlockPos offset : pedestalPositions) {
            BlockPos worldPos = pos.offset(offset);
            BlockEntity blockEntity = level.getBlockEntity(worldPos);
            if (blockEntity instanceof RunePedestalBlockEntity pBE) {
                if (requiredItems.stream().anyMatch(el -> el.test(pBE.getItem()))) {
                    consumedItems.add(pBE.getItemHandler(null).extractItem(0, 1, false));
                }
            }
        }
        if (Utils.compareIngredientsToItems(requiredItems, consumedItems)) {
            return RitualStepResult.SUCCESS;
        }
        RitualUtils.displayMessageToStartingPlayer(
                Component.translatable("ritual.manaweave_and_runes.item_insufficient"), level,
                context);
        return RitualStepResult.ABORT;
    }

    @Override
    public RitualStepResult consumeMana(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context, Ritual.RitualOriginType originType) {
        if (getRitual() != null && ticksPassed % getRitual().getManaRate(level) == 0) {
            Map<Mana, Integer> requiredMana = getRitual().getManaCost(level);
            for (Map.Entry<Mana, Integer> entry : requiredMana.entrySet()) {
                Integer amount = entry.getValue();
                if (manaHandler.extractMana(amount, entry.getKey(), false) != amount) {
                    RitualUtils.displayMessageToStartingPlayer(
                            Component.translatable("ritual.manaweave_and_runes.mana_insufficient"), level, context);
                    return RitualStepResult.ABORT;
                }
            }
            requestRequiredMana();
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

    @Override
    public void cleanUp(Level level, BlockPos pos, BlockState blockState, RitualContext context,
            Ritual.RitualOriginType originType) {
        this.ritualTicks = 0;
        this.pedestalPositions.clear();
        this.pedestalsToVisit.clear();
        this.requiredItems = List.of();
        this.consumedItems.clear();
        this.ritualContext = new RitualContext();
        this.triggerAnim("controller", "idle_active");
    }

    @Override
    public RitualStepResult afterRitualTick(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context, Ritual.RitualOriginType originType) {
        ritualTicks++;

        if (getRitual().getDuration() != -1 && ritualTicks >= getRitual().getDuration()) {
            return RitualStepResult.END;
        }
        return RitualStepResult.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ManaHandler getManaHandler(@Nullable Direction side) {
        return manaHandler;
    }

    @Override
    public ManaNetworkNode getManaNetworkNode() {
        return manaNetworkNode;
    }

    @Override
    public void setManaNetworkNode(ManaNetworkNode node) {
        this.manaNetworkNode = node;
    }

    public boolean checkAndStartRitual(Level level, Player player, ItemStack stack) {
        //TODO: Check if all required extra data is provided
        if (getState() != RitualState.IDLE || !stack.is(MRItemInit.MANA_WEAVER_WAND)) {
            return false;
        }

        List<ItemStack> items = new ArrayList<>();
        RitualContext ritualContext = new RitualContext();
        for (BlockPos offset : getRitualAnchorType().findBlocks(level,
                                                                MRBlockInit.RUNE_PEDESTAL_BLOCK.get())) {
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
        ritualContext.putData("starting_player", new PlayerRitualData(player));

        Ritual ritual = getMatchingRitual(items, getRitualAnchorType().getTier(), ORIGIN_TYPE, level).orElse(null);
        if (ritual == null) {
            RitualUtils.displayMessageToStartingPlayer(
                    Component.translatable("ritual.manaweave_and_runes.unknown_ritual"), level,
                    ritualContext);
            return false;
        }


        AdvancementHolder
                advancement = player.getServer().getAdvancements().get(ritual.getRegistryName().withPrefix("rituals/"));
        if (advancement != null && !player.isCreative() && !((ServerPlayer) player).getAdvancements()
                .getOrStartProgress(advancement)
                .isDone()) {
            RitualUtils.displayMessageToStartingPlayer(
                    Component.translatable("ritual.manaweave_and_runes.unknown_ritual"), level,
                    ritualContext);
            return false;
        }


        this.pedestalPositions =
                getRitualAnchorType().findBlocks(level, MRBlockInit.RUNE_PEDESTAL_BLOCK.get());
        this.pedestalsToVisit = new ArrayList<>(pedestalPositions);
        this.requiredItems = ritual.getInitialItemCost(level);
        this.ritualContext = ritualContext;

        startRitual(ritual);
        triggerAnim("controller", "start_ritual");
        return true;
    }

    public void requestRequiredMana() {
        if (getRitual() != null) {
            Map<Mana, Integer> requiredMana = getRitual().getManaCost(level);
            for (Map.Entry<Mana, Integer> entry : requiredMana.entrySet()) {
                manaNetworkNode.requestMana(entry.getValue(), entry.getKey());
            }
        }
    }

    public RitualAnchorType getRitualAnchorType() {
        return getBlockState().getBlock() instanceof RitualAnchorBlock block ? block.ritualAnchorType : RitualAnchorTypes.NOVICE;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        deserializeNBT(tag.getCompound("current_ritual"), registries);
        ritualTicks = tag.getInt("ritual_ticks");
        if (tag.contains("context")) ritualContext = RitualContext.deserializeNBT(tag.getCompound("context"));
        manaHandler.deserializeNBT(registries, tag.getCompound("mana"));
        manaNetworkNode = ManaNetworkNode.deserializeNBT(tag.getCompound("mana_network_node"), registries, this)
                .orElse(new ManaNetworkNode(this, ManaNetworkNodeType.RECEIVER, 20, true));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("current_ritual", currentRitual != null ? serializeNBT(registries) : new CompoundTag());
        tag.putInt("ritual_ticks", ritualTicks);
        tag.put("context", ritualContext.serializeNBT());
        tag.put("mana", manaHandler.serializeNBT(registries));
        tag.put("mana_network_node", manaNetworkNode.serializeNBT(registries));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        tag.putBoolean("Active", isActive());
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        isActive = tag.contains("Active") && tag.getBoolean("Active");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (manaNetworkNode != null) {
            manaNetworkNode.updateNetwork();
            manaNetworkNode.connectPendingNodes();
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::deployAnimController)
                                .triggerableAnim("activate", ACTIVATION_ANIMATION)
                                .triggerableAnim("idle_active", IDLE_ACTIVE)
                                .triggerableAnim("start_ritual", RUNNING_ANIMATION)
                                .triggerableAnim("deactivate", DEACTIVATION_ANIMATION));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private PlayState deployAnimController(AnimationState<RitualAnchorBlockEntity> state) {
        return state.setAndContinue(DEPLOY_ANIMATION);
    }
}
