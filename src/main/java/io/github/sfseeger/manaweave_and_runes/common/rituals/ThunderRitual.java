package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PlayerRitualData;
import io.github.sfseeger.lib.common.rituals.ritual_data.builtin.PositionRitualData;
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

import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.PLAYER_TYPE;
import static io.github.sfseeger.lib.common.rituals.ritual_data.RitualDataTypes.POSITION_TYPE;

public class ThunderRitual extends Ritual {
    public ThunderRitual() {
        super(Tier.NOVICE, 3 * 20 + 1);
    }

    @Override
    public Vec3 getDimension() {
        return null;
    }

    @Override
    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {
        if (ticksPassed % 20 == 0) {
            PositionRitualData posData = context.getData(POSITION_TYPE);
            if (posData == null) {
                return RitualStepResult.ABORT;
            }
            RandomSource random = level.random;

            BlockPos contextPos = posData.getPos();
            EntityType.LIGHTNING_BOLT.spawn(level, contextPos.offset(random.nextInt(0, 5), 0, random.nextInt(0, 5)),
                                            MobSpawnType.TRIGGERED);
        }
        return RitualStepResult.SUCCESS;
    }

    @Override
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        returnPositionRune(level, pos);
    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
        returnPositionRune(level, pos);

        PlayerRitualData player = context.getData("starting_player", PLAYER_TYPE);
        if (player == null) {
            return;
        }
        Player p = level.getPlayerByUUID(player.getPlayerUUID());
        if (p == null) {
            return;
        }
        BlockPos pPos = p.blockPosition();
        EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, pPos, MobSpawnType.TRIGGERED);
        p.hurt(p.damageSources().magic(), 10);
    }

    public void returnPositionRune(Level level, BlockPos pos) {
        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                                            new ItemStack(MRItemInit.POSITION_RUNE_ITEM.get())));
    }
}
