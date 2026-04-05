package io.github.sfseeger.manaweave_and_runes.common.entity.scrying;

import io.github.sfseeger.manaweave_and_runes.core.payloads.CameraSetPayload;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ChunkTrackingView;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CameraProxyEntity extends Entity {
    public static final EntityType<CameraProxyEntity> TYPE =
            EntityType.Builder.<CameraProxyEntity>of(CameraProxyEntity::new, MobCategory.MISC)
                    .sized(0.0001F, 0.0001F)
                    .setTrackingRange(256)
                    .setUpdateInterval(20)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("camera_proxy");

    private UUID playerToFollowUUID;
    private @Nullable Player playerToFollow;
    private ChunkTrackingView chunkTrackingView;
    private boolean hasSentChunks = false;


    public CameraProxyEntity(EntityType<CameraProxyEntity> entityType, Level level) {
        super(entityType, level);
        noPhysics = true;
    }

    public CameraProxyEntity(Level level, UUID playerToFollowUUID) {
        this(TYPE, level);
        if (!this.level().isClientSide()) {
            ServerPlayer playerToFollow = ((ServerLevel) level).getServer().getPlayerList().getPlayer(playerToFollowUUID);
            if (playerToFollow == null) {
                discard();
                return;
            }
            setPos(playerToFollow.getX(), playerToFollow.getY(), playerToFollow.getZ());
            setRot(playerToFollow.getYRot(), playerToFollow.getXRot());
            this.playerToFollowUUID = playerToFollowUUID;
            this.playerToFollow = playerToFollow;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            if (playerToFollow == null) {
                ServerPlayer playerToFollow = ((ServerLevel) level()).getServer().getPlayerList().getPlayer(playerToFollowUUID);
                if (playerToFollow == null) {
                    discard();
                    return;
                }
                this.playerToFollow = playerToFollow;
            }
            setPos(playerToFollow.getX(), playerToFollow.getY(), playerToFollow.getZ());
            setRot(playerToFollow.getYRot(), playerToFollow.getXRot());
        }
    }

    public void stopViewing(ServerPlayer player) {
        if(!level().isClientSide()) {
            player.setCamera(player);
            PacketDistributor.sendToPlayer(player, new CameraSetPayload(player.getId()));
        }
    }

    public ChunkTrackingView getCameraChunks() {
        return chunkTrackingView;
    }
    public void setChunkLoadingDistance(int chunkLoadingDistance) {
        chunkTrackingView = ChunkTrackingView.of(chunkPosition(), chunkLoadingDistance);
    }
    public boolean hasSentChunks() {
        return hasSentChunks;
    }
    public void setHasSentChunks(boolean hasSentChunks) {
        this.hasSentChunks = hasSentChunks;
    }

    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity);
    }
}
