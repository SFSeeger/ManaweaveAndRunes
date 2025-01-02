package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualStepResult;
import io.github.sfseeger.lib.common.rituals.ritual_data.RitualContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FlightRitual extends Ritual {
    public FlightRitual() {
        super(Tier.MASTER, -1);
    }

    @Override
    public Vec3 getDimension() {
        return new Vec3(30, 30, 30);
    }

    @Override
    public RitualStepResult onRitualServerTick(ServerLevel level, BlockPos pos, BlockState state, int ticksPassed,
            RitualContext context, RitualOriginType originType) {
        if (ticksPassed % 20 == 0) {
            level.getPlayers(p -> p.position().distanceTo(new Vec3(pos.getX(), pos.getY(), pos.getZ())) <= 45)
                    .forEach(player -> {
                        if (player.isAlive() && player.position()
                                .distanceTo(new Vec3(pos.getX(), pos.getY(), pos.getZ())) <= 30) {
                            player.getAbilities().mayfly = true;
                            player.onUpdateAbilities();
                        } else if (!player.isCreative() && !player.isSpectator()) {
                            player.getAbilities().mayfly = false;
                            player.onUpdateAbilities();
                        }
                    });
        }
        return RitualStepResult.SUCCESS;
    }

    // TODO: Attach data to all players granted flight to remove it on end or when not in range
    @Override
    public void onRitualEnd(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
    }

    @Override
    public void onRitualInterrupt(Level level, BlockPos pos, BlockState state, RitualContext context,
            RitualOriginType originType) {
    }
}
