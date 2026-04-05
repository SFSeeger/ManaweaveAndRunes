package io.github.sfseeger.manaweave_and_runes.core.mixin.scrying;


import com.llamalad7.mixinextras.sugar.Local;
import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraProxyEntity;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ChunkTrackingView;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChunkMap.class, priority = 1100)
public abstract class ChunkMapMixin {
    @Shadow
    protected abstract void markChunkPendingToSend(ServerPlayer player, ChunkPos pos);

    @Shadow
    private static void markChunkPendingToSend(ServerPlayer player, LevelChunk chunk) {}

    @Shadow
    abstract int getPlayerViewDistance(ServerPlayer player);

    /**
     * Sends chunks loaded by mounted cameras or frame cameras to the client. Also drops chunks that were near a dismounted
     * camera or a stopped frame camera feed.
     */
    @Inject(method = "updateChunkTracking", at = @At("HEAD"))
    private void manaweave_and_runes$onUpdateChunkTracking(ServerPlayer player, CallbackInfo ci) {
        Level level = player.level();
        int viewDistance = getPlayerViewDistance(player);

        if (player.getCamera() instanceof CameraProxyEntity camera && !camera.hasSentChunks()) {
            ChunkTrackingView.difference(player.getChunkTrackingView(), camera.getCameraChunks(),
                                         chunkPos -> markChunkPendingToSend(player, chunkPos), chunkPos -> {
                    });
            camera.setHasSentChunks(true);
        }
    }

    /**
     * Allows chunks that are forceloaded near a currently active camera to be sent to the player mounting the camera or viewing
     * the camera feed in a frame.
     */
    @Inject(method = "onChunkReadyToSend", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getChunkTrackingView()Lnet/minecraft/server/level/ChunkTrackingView;"))
    private void manaweave_and_runes$sendChunksToCameras(LevelChunk chunk, CallbackInfo ci, @Local ServerPlayer player) {
        ChunkPos pos = chunk.getPos();

        if ((player.getCamera() instanceof CameraProxyEntity camera && camera.getCameraChunks().contains(pos)))
            markChunkPendingToSend(player, chunk);
    }

    /**
     * Fixes block updates not getting sent to chunks around mounted or frame cameras by marking all nearby chunks as tracked
     */
    @Inject(method = "isChunkTracked", at = @At("HEAD"), cancellable = true)
    private void manaweave_and_runes$onIsChunkTracked(ServerPlayer player, int x, int z, CallbackInfoReturnable<Boolean> cir) {
        if (player.getCamera() instanceof CameraProxyEntity camera && camera.getCameraChunks().contains(x, z)) {
            cir.setReturnValue(true);
        }
    }
}
