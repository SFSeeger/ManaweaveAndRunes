package io.github.sfseeger.manaweave_and_runes.core.mixin.scrying;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraProxyEntity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Minecraft.class, priority = 1100)
public abstract class MinecraftMixin {
    /**
     * Disallows players from pressing F5 (by default) to change to third person while being mounted to a camera
     */
    @WrapWithCondition(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;setCameraType(Lnet/minecraft/client/CameraType;)V"))
    private boolean manaweave_and_runes$mayChangeCameraType(Options options, CameraType newType) {
        return !(Minecraft.getInstance().cameraEntity instanceof CameraProxyEntity);
    }

}
