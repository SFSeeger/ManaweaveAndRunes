package io.github.sfseeger.manaweave_and_runes.core.mixin.scrying;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraProxyEntity;
import io.github.sfseeger.manaweave_and_runes.core.util.PlayerUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * When a player is mounted to a camera, enables sounds near the camera to be played, while sounds near the player entity are
 * suppressed
 */
@Mixin(value = PlayerList.class, priority = 1100)
public abstract class PlayerListMixin {
    @Inject(method = "broadcast", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerPlayer;getZ()D"), cancellable = true)
    private void manaweave_and_runes$broadcastToCameraProxies(Player except, double x, double y, double z, double radius, ResourceKey<Level> dimension, Packet<?> packet, CallbackInfo ci, @Local ServerPlayer player) {
        if (PlayerUtils.isPlayerScrying(player)) {
            CameraProxyEntity camera = (CameraProxyEntity) player.getCamera();
            double dX = x - camera.getX();
            double dY = y - camera.getY();
            double dZ = z - camera.getZ();

            if (dX * dX + dY * dY + dZ * dZ < radius * radius)
                player.connection.send(packet);

            ci.cancel();
        }
    }
}
