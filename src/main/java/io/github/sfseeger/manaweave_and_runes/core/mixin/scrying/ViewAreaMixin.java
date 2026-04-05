package io.github.sfseeger.manaweave_and_runes.core.mixin.scrying;

import net.minecraft.client.renderer.ViewArea;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Mixin(ViewArea.class)
//public abstract class ViewAreaMixin {
//    @Inject(method = "setDirty", at = @At("HEAD"))
//    private void manaweave_and_runes$onSetChunkDirty(int cx, int cy, int cz, boolean reRenderOnMainThread, CallbackInfo ci) {
//        CameraViewAreaExtension.setDirty(cx, cy, cz, reRenderOnMainThread);
//    }
//}
