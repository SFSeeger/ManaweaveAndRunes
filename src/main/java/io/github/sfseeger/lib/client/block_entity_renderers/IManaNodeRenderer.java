package io.github.sfseeger.lib.client.block_entity_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.sfseeger.lib.client.utils.AssetUtils;
import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import io.github.sfseeger.lib.common.mana.network.ManaNetworkNode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

public interface IManaNodeRenderer {
    default void renderManaConnections(IManaNetworkSubscriber be, BlockPos pos, PoseStack poseStack,
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
                                             0xFFFFFF, 0.2f, 0.05f); //0.2f
                poseStack.popPose();
            }
        }
    }
}
