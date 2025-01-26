package io.github.sfseeger.lib.client.block_entity_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.sfseeger.lib.client.utils.AssetUtils;
import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaNodeRenderer <T extends BlockEntity> implements BlockEntityRenderer<T> {
    public ManaNodeRenderer(BlockEntityRendererProvider.Context ctx) {
        super();
    }

    public void renderManaConnections(IManaNetworkSubscriber be, BlockPos pos, PoseStack poseStack,
            MultiBufferSource multiBufferSource) {
        if (!be.getManaNetworkNode().getConnectedNodes().isEmpty()) {
            for (ManaNetworkNode node : be.getManaNetworkNode().getConnectedNodes()) {
                BlockPos connectedPos = node.getBlockPos();

                BlockPos offsetStart = BlockPos.ZERO;
                BlockPos offsetEnd = connectedPos.subtract(pos);
                offsetEnd = offsetEnd.rotate(Rotation.CLOCKWISE_90);
                poseStack.pushPose();
                AssetUtils.renderManaThreads(poseStack, multiBufferSource,
                                             new Vec3(offsetStart.getX(), offsetStart.getY(), offsetStart.getZ()),
                                             new Vec3(offsetEnd.getX(), offsetEnd.getY(), offsetEnd.getZ()), 120,
                                             0xFFFFFF, 0.2f, 0.05f);
                poseStack.popPose();
            }
        }
    }

    @Override
    public void render(T blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        renderManaConnections((IManaNetworkSubscriber) blockEntity, blockEntity.getBlockPos(), poseStack, multiBufferSource);
    }

    @Override
    public boolean shouldRender(T blockEntity, Vec3 cameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return true;
    }
}
