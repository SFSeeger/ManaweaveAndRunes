package io.github.sfseeger.manaweave_and_runes.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.sfseeger.lib.client.ber.ManaNodeRenderer;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaStorageBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ManaStorageBlockEntityRenderer extends ManaNodeRenderer implements BlockEntityRenderer<ManaStorageBlockEntity> {
    private final BlockEntityRendererProvider.Context context;


    public ManaStorageBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ManaStorageBlockEntity manaStorageBlockEntity, float v, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        renderManaConnections(manaStorageBlockEntity, manaStorageBlockEntity.getBlockPos(), poseStack,
                              multiBufferSource);
    }
}
