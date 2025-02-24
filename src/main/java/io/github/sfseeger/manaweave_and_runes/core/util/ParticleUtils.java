package io.github.sfseeger.manaweave_and_runes.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class ParticleUtils {
    /**
     * Generates a random position inside a box defined by two relative corners to the pos.
     * Inside the block at pos, the two corners would be (0, 0, 0) & (1, 1, 1).
     */
    public static Vec3 randomPosInsideBox(BlockPos pos, RandomSource random, double x1, double y1, double z1, double x2,
            double y2, double z2) {
        return Vec3.atLowerCornerOf(pos)
                .add(Mth.nextDouble(random, x1, x2), Mth.nextDouble(random, y1, y2), Mth.nextDouble(random, z1, z2));
    }

    public static Vec3 randomPosInsideSphere(BlockPos pos, RandomSource random, double radius) {
        Vec3 center = Vec3.atLowerCornerOf(pos);

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = random.nextDouble();

        double theta = 2 * Mth.PI * u;  // Random azimuthal angle
        double phi = Math.acos(2 * v - 1); // Random polar angle
        double r = Math.cbrt(w) * radius; // Uniform distribution in sphere

        double x = r * Math.sin(phi) * Math.cos(theta);
        double y = r * Math.sin(phi) * Math.sin(theta);
        double z = r * Math.cos(phi);

        return center.add(new Vec3(x, y, z));
    }
}
