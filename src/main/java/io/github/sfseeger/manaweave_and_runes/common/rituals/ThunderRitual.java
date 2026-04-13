package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.context_data_types.builtin.PlayerContextDataType;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStateMachineContext;
import io.github.sfseeger.lib.common.rituals.state_machine.RitualStepResult;
import io.github.sfseeger.lib.common.context_data_types.ContextMap;
import io.github.sfseeger.lib.common.context_data_types.builtin.PositionContextDataType;
import io.github.sfseeger.manaweave_and_runes.common.MRDamageTypes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static io.github.sfseeger.lib.common.context_data_types.ContextDataTypes.PLAYER_TYPE;
import static io.github.sfseeger.lib.common.context_data_types.ContextDataTypes.POSITION_TYPE;

public class ThunderRitual extends Ritual {
    public ThunderRitual() {
        super(Tier.NOVICE, 3 * 20 + 1);
    }

    @Override
    public Vec3 getDimension() {
        return null;
    }

    @Override
    public RitualStepResult onRitualServerTick(RitualStateMachineContext ctx) {
        if (ctx.ticksPassed() % 20 == 0) {
            PositionContextDataType posData = ctx.contextMap().getData(POSITION_TYPE).orElse(null);
            if (posData == null) {
                return RitualStepResult.FAIL;
            }
            RandomSource random = ctx.level().random;

            BlockPos contextPos = posData.pos();
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) ctx.level(), contextPos.offset(random.nextInt(0, 5), 0, random.nextInt(0, 5)),
                                            MobSpawnType.TRIGGERED);
        }
        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(RitualStateMachineContext ctx) {
        returnPositionRune(ctx.level(), ctx.pos());
    }

    @Override
    public void onRitualAbort(Level level, BlockPos pos, BlockState state, ContextMap context,
                              RitualOriginType originType) {
        returnPositionRune(level, pos);

        PlayerContextDataType player = context.getData("starting_player", PLAYER_TYPE).orElse(null);
        if (player == null) {
            return;
        }
        Player p = level.getPlayerByUUID(player.getPlayerUUID());
        if (p == null) {
            return;
        }
        BlockPos pPos = p.blockPosition();
        EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, pPos, MobSpawnType.TRIGGERED);
        p.hurt(MRDamageTypes.createRitualFailure(level, pos), 5);
    }

    public void returnPositionRune(Level level, BlockPos pos) {
        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                                            new ItemStack(MRItemInit.POSITION_RUNE_ITEM.get())));
    }
}
