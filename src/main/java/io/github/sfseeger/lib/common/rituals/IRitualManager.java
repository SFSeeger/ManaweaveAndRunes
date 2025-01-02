package io.github.sfseeger.lib.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.core.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IRitualManager {
    Map<RitualState, Map<RitualStepResult, RitualState>> transitionMap = Map.of(
            RitualState.IDLE, Map.of(
                    RitualStepResult.SUCCESS, RitualState.START,
                    RitualStepResult.SKIP, RitualState.IDLE,
                    RitualStepResult.END, RitualState.IDLE,
                    RitualStepResult.ABORT, RitualState.IDLE
            ),
            RitualState.START, Map.of(
                    RitualStepResult.SUCCESS, RitualState.INITIAL_ITEM_CONSUME,
                    RitualStepResult.SKIP, RitualState.TICK,
                    RitualStepResult.END, RitualState.FINISH,
                    RitualStepResult.ABORT, RitualState.ABORT
            ),
            RitualState.INITIAL_ITEM_CONSUME, Map.of(
                    RitualStepResult.SUCCESS, RitualState.INITIAL_ITEM_CONSUME,
                    RitualStepResult.SKIP, RitualState.TICK,
                    RitualStepResult.END, RitualState.FINISH,
                    RitualStepResult.ABORT, RitualState.ABORT
            ),
            RitualState.TICK, Map.of(
                    RitualStepResult.SUCCESS, RitualState.TICK,
                    RitualStepResult.SKIP, RitualState.TICK,
                    RitualStepResult.END, RitualState.FINISH,
                    RitualStepResult.ABORT, RitualState.ABORT
            ),
            RitualState.FINISH, Map.of(
                    RitualStepResult.SUCCESS, RitualState.IDLE,
                    RitualStepResult.SKIP, RitualState.IDLE,
                    RitualStepResult.END, RitualState.IDLE,
                    RitualStepResult.ABORT, RitualState.IDLE
            ),
            RitualState.ABORT, Map.of(
                    RitualStepResult.SUCCESS, RitualState.IDLE,
                    RitualStepResult.SKIP, RitualState.IDLE,
                    RitualStepResult.END, RitualState.IDLE,
                    RitualStepResult.ABORT, RitualState.IDLE
            )
    );

    default Optional<Ritual> getMatchingRitual(List<ItemStack> items, Tier tier, Ritual.RitualOriginType originType,
            Level level) {
        return ManaweaveAndRunesRegistries.RITUAL_REGISTRY.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(ritual -> ritual.matches(items, tier, originType, level))
                .findFirst();
    }

    default void transition(RitualStepResult result) {
        RitualState state = getState();
        setState(transitionMap.get(getState()).get(result));
        if (!getState().equals(state)) markUpdated();
    }

    default void startRitual(Ritual ritual) {
        setState(RitualState.START);
        setRitual(ritual);
        markUpdated();
    }

    RitualStepResult consumeInitialItem(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context,
            Ritual.RitualOriginType originType);

    RitualStepResult consumeTickItem(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context,
            Ritual.RitualOriginType originType);

    RitualStepResult consumeMana(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context,
            Ritual.RitualOriginType originType);

    default RitualStepResult executeStep(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context,
            Ritual.RitualOriginType originType) {
        return getState().step(this, level, pos, blockState, ticksPassed, context, originType);
    }

    default void executeStepAndTransition(Level level, BlockPos pos, BlockState blockState, int ticksPassed,
            RitualContext context,
            Ritual.RitualOriginType originType) {
        RitualStepResult result = executeStep(level, pos, blockState, ticksPassed, context, originType);
        transition(result);
    }

    default Tag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("state", getState().ordinal());
        tag.put("ritual", Utils.encode(Ritual.CODEC, getRitual(), provider));

        return tag;
    }

    default void deserializeNBT(CompoundTag tag, HolderLookup.Provider holderLookup) {
        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, holderLookup);

        int state = tag.contains("state") ? tag.getInt("state") : RitualState.IDLE.ordinal();
        setState(RitualState.values()[state]);
        setRitual(Ritual.CODEC.parse(ops, tag.get("ritual"))
                          .result()
                          .orElse(null));
    }

    void markUpdated();

    Ritual getRitual();

    void setRitual(Ritual ritual);

    RitualState getState();

    void setState(RitualState state);


    enum RitualState {
        START((manager, level, pos, blockState, ticksPassed, context, originType) -> {
            RitualStepResult result = manager.getRitual().onRitualStart(level, pos, blockState, context, originType);
            manager.transition(result);
            return result;
        }),
        INITIAL_ITEM_CONSUME(IRitualManager::consumeInitialItem),
        TICK((manager, level, pos, blockState, ticksPassed, context, originType) -> {
            RitualStepResult currentResult =
                    manager.consumeMana(level, pos, blockState, ticksPassed, context, originType);
            currentResult = currentResult.getHigherPriority(
                    manager.getRitual()
                            .onRitualServerTick((ServerLevel) level, pos, blockState, ticksPassed, context,
                                                originType));
            currentResult = currentResult.getHigherPriority(
                    manager.consumeTickItem(level, pos, blockState, ticksPassed, context, originType));
            return currentResult;
        }),
        FINISH((manager, level, pos, blockState, ticksPassed, context, originType) -> {
            manager.getRitual().onRitualEnd(level, pos, blockState, context, originType);
            return RitualStepResult.SUCCESS;
        }),
        ABORT((manager, level, pos, blockState, ticksPassed, context, originType) -> {
            manager.getRitual().onRitualInterrupt(level, pos, blockState, context, originType);
            return RitualStepResult.SUCCESS;
        }),
        IDLE((manager, level, pos, blockState, ticksPassed, extraData, originType) -> RitualStepResult.SKIP);

        private final RitualStep stepFunction;

        RitualState(RitualStep stepFunction) {
            this.stepFunction = stepFunction;
        }

        public RitualStepResult step(IRitualManager manager, Level level, BlockPos pos, BlockState blockState,
                int ticksPassed, RitualContext context,
                Ritual.RitualOriginType originType) {
            return stepFunction.step(manager, level, pos, blockState, ticksPassed, context, originType);
        }


        @FunctionalInterface
        private interface RitualStep {
            RitualStepResult step(IRitualManager manager, Level level, BlockPos pos, BlockState blockState,
                    int ticksPassed, RitualContext context, Ritual.RitualOriginType originType);
        }
    }
}
