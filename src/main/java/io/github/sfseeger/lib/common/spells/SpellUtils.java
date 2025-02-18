package io.github.sfseeger.lib.common.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class SpellUtils {
    //From https://github.com/baileyholl/Ars-Nouveau

    public static HitResult rayTrace(Entity entity, double length, float lookOffset, boolean hitLiquids) {
        HitResult result = entity.pick(length, lookOffset, hitLiquids);
        EntityHitResult entityLookedAt = getLookedAtEntity(entity, 25);
        return entityLookedAt == null ? result : entityLookedAt;
    }

    public static @Nullable EntityHitResult getLookedAtEntity(Entity entity, int range) {
        Vec3 vec3d = entity.getEyePosition(1.0f);
        Vec3 vec3d1 = entity.getViewVector(1.0F);
        Vec3 vec3d2 = vec3d.add(vec3d1.x * range, vec3d1.y * range, vec3d1.z * range);
        AABB axisalignedbb = entity.getBoundingBox().expandTowards(vec3d1.scale(range)).inflate(1.0D, 1.0D, 1.0D);
        return traceEntities(entity, vec3d, vec3d2, axisalignedbb, (e) -> !e.isSpectator() && e.isPickable(), range);
    }
    public static @Nullable EntityHitResult traceEntities(Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance) {
        Level world = shooter.level();
        double d0 = distance;
        Entity entity = null;
        Vec3 vec3d = null;

        for (Entity entity1 : world.getEntities(shooter, boundingBox, filter)) {
            AABB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);
            if (axisalignedbb.contains(startVec)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    vec3d = optional.orElse(startVec);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec3d1 = optional.get();
                double d1 = startVec.distanceToSqr(vec3d1);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            vec3d = vec3d1;
                        }
                    } else {
                        entity = entity1;
                        vec3d = vec3d1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, vec3d);
    }


    public static boolean canChangeBlockState(BlockPos pos, SpellCastingContext context) {
        Level level = context.getLevel();
        if (level == null) return false;
        if (level.isClientSide) {
            return false;
        }
        if (!level.getWorldBorder().isWithinBounds(pos)) {
            return false;
        }
        MinecraftServer server = level.getServer();
        if (server != null) {
            if (context.getCaster() instanceof Player player) {
                if (server.isUnderSpawnProtection((ServerLevel) level, pos, player)) {
                    return false;
                }
            } else {
                if (!server.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) return false;
                BlockPos blockpos = level.getSharedSpawnPos();
                int i = Mth.abs(pos.getX() - blockpos.getX());
                int j = Mth.abs(pos.getZ() - blockpos.getZ());
                int k = Math.max(i, j);
                return k <= server.getSpawnProtectionRadius();
            }
        }
        return true;
    }

    public static boolean executeOnPlane(BlockPos pos, SpellCastingContext context, Direction direction,
            Function<BlockPos, Boolean> function) {
        return executeOnPlane(pos, context, direction, 1, function);
    }

    public static boolean executeOnPlane(BlockPos pos, SpellCastingContext context, Direction direction, int steps,
            Function<BlockPos, Boolean> function) {
        int width = context.getVariableSave("width", 1);
        int width2 = width / 2;
        int height = context.getVariableSave("height", 1);
        int height2 = height / 2;

        Vec3i b1 = new Vec3i(0, 0, 1);
        Vec3i b2 = new Vec3i(0, 1, 0);
        switch (direction.getAxis()) {
            case Direction.Axis.X -> b1 = new Vec3i(0, 0, 1);
            case Direction.Axis.Y -> {
                int i = context.getCaster().getDirection().getAxis() == Direction.Axis.Z ? 1 : 0;
                b1 = new Vec3i(i, 0, 1 - i);
                b2 = new Vec3i(1 - i, 0, i);
            }
            case Direction.Axis.Z -> b1 = new Vec3i(1, 0, 0);
        }


        boolean flag = false;
        for (int x1 = -width2; x1 <= width2; x1 += steps) {
            for (int x2 = -height2; x2 <= height2; x2 += steps) {
                BlockPos pos1 = pos.offset(b1.multiply(x1)).offset(b2.multiply(x2));

                flag |= function.apply(pos1);
            }
        }
        return flag;
    }
}
