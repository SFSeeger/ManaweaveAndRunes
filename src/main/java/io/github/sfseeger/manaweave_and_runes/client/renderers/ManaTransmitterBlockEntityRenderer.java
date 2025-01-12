package io.github.sfseeger.manaweave_and_runes.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.sfseeger.lib.client.ber.ManaNodeRenderer;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaTransmitterBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaTransmitterBlockEntityRenderer extends ManaNodeRenderer implements BlockEntityRenderer<ManaTransmitterBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public ManaTransmitterBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ManaTransmitterBlockEntity be, float v, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        renderManaConnections(be, be.getBlockPos(), poseStack, multiBufferSource);
    }

    @Override
    public boolean shouldRender(ManaTransmitterBlockEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(ManaTransmitterBlockEntity blockEntity) {
        return !blockEntity.getManaNetworkNode().getConnectedNodes().isEmpty();
    }
}
