package io.github.sfseeger.manaweave_and_runes.core.mixin.scrying;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.sfseeger.manaweave_and_runes.core.util.PlayerUtils;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    /**
     * Fixes players mounted to cameras not sending movement packets to the server, which causes them to be immovable from
     * the server's perspective
     */
    @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isControlledCamera()Z"))
    private boolean manaweave_and_runes$onIsControlledCamera(LocalPlayer player, Operation<Boolean> original) {
        if (PlayerUtils.isPlayerScrying(player))
            return true;

        return original.call(player);
    }
}
