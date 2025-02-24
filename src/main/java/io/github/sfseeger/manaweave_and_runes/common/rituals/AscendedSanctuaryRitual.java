package io.github.sfseeger.manaweave_and_runes.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class AscendedSanctuaryRitual extends SanctuaryRitual {
    public AscendedSanctuaryRitual() {
        super(Tier.ASCENDED);
    }

    @Override
    public Vec3 getDimension() {
        return new Vec3(64, 64, 64);
    }

    @Override
    protected void addEffectToPlayer(Player player) {
        super.addEffectToPlayer(player);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 2, true, false));
    }
}
