package io.github.sfseeger.manaweave_and_runes.core.mixin.scrying;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.sfseeger.manaweave_and_runes.core.util.PlayerUtils;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Enables entities that are in range of a player-viewed camera, as well as the mounted security camera entity, to be sent to
 * the client.
 */
@Mixin(value = ChunkMap.TrackedEntity.class, priority = 1100)
public abstract class TrackedEntityMixin {
    @Shadow
    @Final
    Entity entity;

    @ModifyVariable(method = "updatePlayer", name = "flag", at = @At(value = "JUMP", opcode = Opcodes.IFEQ, shift = At.Shift.BEFORE, ordinal = 2))
    private boolean manaweave_and_runes$modifyFlag(boolean original, ServerPlayer player, @Local(ordinal = 0) double viewDistance) {
        if (original)
            return true;

        Entity camera = player.getCamera();

        if (PlayerUtils.isPlayerScrying(player)) {
            if (entity == player.getCamera()) //If the player is mounted to a camera entity, that entity always needs to be sent to the client regardless of distance
                return true;

            Vec3 relativePosToCamera = camera.position().subtract(entity.position());

            return relativePosToCamera.x >= -viewDistance && relativePosToCamera.x <= viewDistance && relativePosToCamera.z >= -viewDistance && relativePosToCamera.z <= viewDistance;
        }

        return false;
    }
}
