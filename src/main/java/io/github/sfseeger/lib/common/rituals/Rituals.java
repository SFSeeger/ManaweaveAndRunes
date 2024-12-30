package io.github.sfseeger.lib.common.rituals;

import io.github.sfseeger.lib.common.Tier;
import net.minecraft.world.phys.Vec3;

public class Rituals {
    public static final Ritual DEFAULT_RITUAL = new Ritual(Tier.NOVICE, 0) {
        @Override
        public Vec3 getDimension() {
            return Vec3.ZERO;
        }
    };
}
