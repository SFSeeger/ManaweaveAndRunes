package io.github.sfseeger.manaweave_and_runes.marks;

import io.github.sfseeger.lib.common.rituals.marks.Mark;
import io.github.sfseeger.lib.common.rituals.marks.MarkInstance;
import io.github.sfseeger.lib.common.rituals.marks.MarkType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MarkOfOverheating extends Mark {
    @Override
    public void applyEffect(MarkInstance markInstance, Player player) {
        Level level = player.level();
        RandomSource random = level.random;
        if (!level.isClientSide && random.nextInt(500) == 0) {
            if (level.getBiomeManager()
                    .getBiome(player.blockPosition())
                    .value()
                    .getBaseTemperature() > 1.5 && !(level.isRaining() || level.isThundering())) {
                player.igniteForTicks(60);
            }
        }
    }

    @Override
    public MarkType getMarkType() {
        return MarkType.CURSE;
    }
}
