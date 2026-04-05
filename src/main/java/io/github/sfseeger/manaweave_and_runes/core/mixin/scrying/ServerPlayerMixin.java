package io.github.sfseeger.manaweave_and_runes.core.mixin.scrying;


import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraProxyEntity;
import io.github.sfseeger.manaweave_and_runes.core.util.PlayerUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayer.class, priority = 1100)
public abstract class ServerPlayerMixin {
    @Shadow
    public abstract Entity getCamera();

    /**
     * Makes sure the server does not move the player mounting a camera to the camera's position
     */
    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;absMoveTo(DDDFF)V"))
    private boolean manaweave_and_runes$shouldMoveTo(ServerPlayer player, double x, double y, double z, float yaw, float pitch) {
        return !PlayerUtils.isPlayerScrying(player);
    }

    /**
     * Ensures that players in spectator mode are able to see players that are currently viewing a camera, because players
     * that are spectating another entity are usually invisible for other spectators
     */
    @Inject(method = "broadcastToPlayer", at = @At("HEAD"), cancellable = true)
    private void manaweave_and_runes$broadcastPlayerViewingCamera(ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (getCamera() instanceof CameraProxyEntity && player.isSpectator())
            cir.setReturnValue(true);
    }
}
