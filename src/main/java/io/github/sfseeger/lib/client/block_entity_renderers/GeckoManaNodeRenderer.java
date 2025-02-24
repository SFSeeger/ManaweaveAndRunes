package io.github.sfseeger.lib.client.block_entity_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.sfseeger.lib.common.mana.IManaNetworkSubscriber;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GeckoManaNodeRenderer<T extends BlockEntity & GeoAnimatable & IManaNetworkSubscriber> extends GeoBlockRenderer<T> implements IManaNodeRenderer {
    public GeckoManaNodeRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType,
            MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
            int packedLight, int packedOverlay, int colour) {

        poseStack.pushPose();
        poseStack.translate(-0.5, -0.5, -0.5);
        renderManaConnections(animatable, animatable.getBlockPos(), poseStack, bufferSource);
        poseStack.popPose();

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick,
                             packedLight, packedOverlay, colour);
    }
}
