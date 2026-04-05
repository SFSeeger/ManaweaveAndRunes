package io.github.sfseeger.manaweave_and_runes.core.mixin.scrying;

import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraClientChunkCacheExtension;
import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraProxyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(value = ClientChunkCache.class, priority = 1100)
public abstract class ClientChunkCacheMixin {
    @Shadow
    volatile ClientChunkCache.Storage storage;

    @Shadow
    @Final
    ClientLevel level;
    /**
     * Removes dropped chunks from the camera client chunk cache, unless the chunk is in range of a currently mounted camera or a
     * frame camera
     */
    @Inject(method = "drop", at = @At("HEAD"))
    private void manaweave_and_runes$onDrop(ChunkPos pos, CallbackInfo ci) {
        int renderDistance = Minecraft.getInstance().options.renderDistance().get();
        Entity cameraEntity = Minecraft.getInstance().cameraEntity;

        if (cameraEntity instanceof CameraProxyEntity && pos.getChessboardDistance(cameraEntity.chunkPosition()) <= (renderDistance + 1))
            return;

        CameraClientChunkCacheExtension.drop(level, pos);
    }
    /**
     * Places clientside received chunks which are in range of a mounted camera or a frame camera into the camera client chunk
     * cache
     */
    @Inject(method = "replaceWithPacketData", at = @At("HEAD"), cancellable = true)
    private void manaweave_and_runes$onReplaceChunk(int x, int z, FriendlyByteBuf buffer, CompoundTag chunkTag, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> tagOutputConsumer, CallbackInfoReturnable<LevelChunk> cir) {
        int renderDistance = Minecraft.getInstance().options.renderDistance().get();
        Entity cameraEntity = Minecraft.getInstance().cameraEntity;
        ChunkPos pos = new ChunkPos(x, z);
        boolean isInPlayerRange = storage.inRange(x, z);
        boolean shouldAddChunk = cameraEntity instanceof CameraProxyEntity && pos.getChessboardDistance( cameraEntity.chunkPosition()) <= (renderDistance + 1);

        if (shouldAddChunk) {
            LevelChunk newChunk = CameraClientChunkCacheExtension.replaceWithPacketData(level, x, z, new FriendlyByteBuf(buffer.copy()), chunkTag, tagOutputConsumer);

            if (!isInPlayerRange)
                cir.setReturnValue(newChunk);
        }
    }

    /**
     * If the requested chunk is absent in the vanilla storage but present in the camera client chunk cache, return that chunk
     */
    @Inject(method = "getChunk(IILnet/minecraft/world/level/chunk/status/ChunkStatus;Z)Lnet/minecraft/world/level/chunk/LevelChunk;", at = @At("TAIL"), cancellable = true)
    private void manaweave_and_runes$onGetChunk(int x, int z, ChunkStatus requiredStatus, boolean requireChunk, CallbackInfoReturnable<LevelChunk> cir) {
        if (!storage.inRange(x, z)) {
            LevelChunk chunk = CameraClientChunkCacheExtension.getChunk(x, z);

            if (chunk != null)
                cir.setReturnValue(chunk);
        }
    }
}

