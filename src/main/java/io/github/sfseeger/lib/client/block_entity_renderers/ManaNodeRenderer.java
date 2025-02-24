package io.github.sfseeger.lib.client.block_entity_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaNodeRenderer<T extends BlockEntity & IManaNetworkSubscriber> implements BlockEntityRenderer<T>, IManaNodeRenderer {
    public ManaNodeRenderer(BlockEntityRendererProvider.Context ctx) {
        super();
    }


    @Override
    public void render(T blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        renderManaConnections(blockEntity, blockEntity.getBlockPos(), poseStack, multiBufferSource);
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(T blockEntity, Vec3 cameraPos) {
        return Vec3.atCenterOf(blockEntity.getBlockPos())
                .multiply(1.0, 0.0, 1.0)
                .closerThan(cameraPos.multiply(1.0, 0.0, 1.0), (double) this.getViewDistance());
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), (double) pos.getX() + 1.0, 1024.0,
                        (double) pos.getZ() + 1.0);
    }
}
