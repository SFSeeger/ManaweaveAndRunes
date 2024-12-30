package io.github.sfseeger.lib.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import io.github.sfseeger.manaweave_and_runes.core.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class RitualManager {
    public static final Map<RitualState, Map<RitualStepResult, RitualState>> transitionMap = Map.of(
            RitualState.START, Map.of(
                    RitualStepResult.SUCCESS, RitualState.INITIAL_ITEM_CONSUME,
                    RitualStepResult.SKIP, RitualState.MANA_CONSUME,
                    RitualStepResult.END, RitualState.FINISH,
                    RitualStepResult.ABORT, RitualState.ABORT
            ),
            RitualState.INITIAL_ITEM_CONSUME, Map.of(
                    RitualStepResult.SUCCESS, RitualState.MANA_CONSUME,
                    RitualStepResult.SKIP, RitualState.TICK,
                    RitualStepResult.END, RitualState.FINISH,
                    RitualStepResult.ABORT, RitualState.ABORT
            ),
            RitualState.MANA_CONSUME, Map.of(
                    RitualStepResult.SUCCESS, RitualState.TICK,
                    RitualStepResult.SKIP, RitualState.TICK,
                    RitualStepResult.END, RitualState.FINISH,
                    RitualStepResult.ABORT, RitualState.ABORT
            ),
            RitualState.TICK, Map.of(
                    RitualStepResult.SUCCESS, RitualState.TICK_ITEM_CONSUME,
                    RitualStepResult.SKIP, RitualState.MANA_CONSUME,
                    RitualStepResult.END, RitualState.FINISH,
                    RitualStepResult.ABORT, RitualState.ABORT
            ),
            RitualState.TICK_ITEM_CONSUME, Map.of(
                    RitualStepResult.SUCCESS, RitualState.MANA_CONSUME,
                    RitualStepResult.SKIP, RitualState.TICK,
                    RitualStepResult.END, RitualState.FINISH,
                    RitualStepResult.ABORT, RitualState.ABORT
            )
    );
    RitualState state;
    Ritual ritual;

    public Optional<Ritual> getMatchingRitual(List<Ingredient> items, Tier tier, Ritual.RitualOriginType originType,
            Level level) {
        return ManaweaveAndRunesRegistries.RITUAL_REGISTRY.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(ritual -> ritual.matches(items, tier, originType, level))
                .findFirst();
    }

    public void transition(RitualStepResult result) {
        if (state == RitualState.FINISH || state == RitualState.ABORT) {
            return;
        }
        state = transitionMap.get(state).get(result);
    }

    public void startRitual(Ritual ritual) {
        state = RitualState.START;
        this.ritual = ritual;
    }

    public abstract RitualStepResult consumeInitialItem(Level level, BlockPos pos, BlockState blockState,
            Ritual.RitualOriginType originType);

    public abstract RitualStepResult consumeTickItem(Level level, BlockPos pos, BlockState blockState,
            Ritual.RitualOriginType originType);

    public abstract RitualStepResult consumeMana(Level level, BlockPos pos, BlockState blockState,
            Ritual.RitualOriginType originType);

    public RitualStepResult executeStep(Level level, BlockPos pos, BlockState blockState,
            Ritual.RitualOriginType originType) {
        return state.step(this, level, pos, blockState, originType);
    }

    public void executeStepAndTransition(Level level, BlockPos pos, BlockState blockState,
            Ritual.RitualOriginType originType) {
        RitualStepResult result = executeStep(level, pos, blockState, originType);
        transition(result);
    }

    public Tag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("state", state.ordinal());
        tag.put("ritual", Utils.encode(Ritual.CODEC, ritual, provider));

        return tag;
    }

    public void deserializeNBT(CompoundTag tag, HolderLookup.Provider holderLookup) {
        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, holderLookup);

        state = RitualState.values()[tag.getInt("state")];
        ritual = Ritual.CODEC.parse(ops, tag.get("ritual"))
                .result()
                .orElseThrow(() -> new IllegalArgumentException("Could not deserialize ritual"));
    }


    public enum RitualState {
        START((manager, level, pos, blockState, originType) -> {
            RitualStepResult result = manager.ritual.onRitualStart(level, pos, blockState, originType);
            manager.transition(result);
            return result;
        }),
        INITIAL_ITEM_CONSUME(RitualManager::consumeInitialItem),
        TICK_ITEM_CONSUME(RitualManager::consumeTickItem),
        MANA_CONSUME(RitualManager::consumeMana),
        TICK((manager, level, pos, blockState, originType) ->
                     manager.ritual.onRitualTick(level, pos, blockState, originType)
        ),
        FINISH((manager, level, pos, blockState, originType) -> {
            manager.ritual.onRitualEnd(level, pos, blockState, originType);
            return RitualStepResult.SUCCESS;
        }),
        ABORT((manager, level, pos, blockState, originType) -> {
            manager.ritual.onRitualInterrupt(level, pos, blockState, originType);
            return RitualStepResult.SUCCESS;
        });

        private final RitualStep stepFunction;

        RitualState(RitualStep stepFunction) {
            this.stepFunction = stepFunction;
        }

        public RitualStepResult step(RitualManager manager, Level level, BlockPos pos, BlockState blockState,
                Ritual.RitualOriginType originType) {
            return stepFunction.step(manager, level, pos, blockState, originType);
        }


        @FunctionalInterface
        private interface RitualStep {
            RitualStepResult step(RitualManager manager, Level level, BlockPos pos, BlockState blockState,
                    Ritual.RitualOriginType originType);
        }
    }
}
