package io.github.sfseeger.manaweave_and_runes.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.sfseeger.lib.client.ber.ManaNodeRenderer;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RitualAnchorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RitualAnchorBlockEntityRenderer extends ManaNodeRenderer implements BlockEntityRenderer<RitualAnchorBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public RitualAnchorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(RitualAnchorBlockEntity ritualAnchorBlockEntity, float v, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int i, int i1) {
        renderManaConnections(ritualAnchorBlockEntity, ritualAnchorBlockEntity.getBlockPos(), poseStack,
                              multiBufferSource);
    }

    @Override
    public boolean shouldRenderOffScreen(RitualAnchorBlockEntity blockEntity) {
        return !blockEntity.getManaNetworkNode().getConnectedNodes().isEmpty();
    }
}
