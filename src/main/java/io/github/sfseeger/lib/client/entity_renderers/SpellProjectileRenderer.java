package io.github.sfseeger.lib.client.entity_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.sfseeger.lib.client.RenderTypes;
import io.github.sfseeger.lib.client.models.SpellProjectileModel;
import io.github.sfseeger.lib.common.entities.projectiles.SpellProjectileEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class SpellProjectileRenderer extends EntityRenderer<SpellProjectileEntity> {
    private SpellProjectileModel model;

    public SpellProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new SpellProjectileModel(context.bakeLayer(SpellProjectileModel.LAYER_LOCATION));
    }

    @Override
    public void render(SpellProjectileEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        int lightmap = LightTexture.pack(0xF000F0, 0xF000F0);
        poseStack.pushPose();
        VertexConsumer builder =
                ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(this.getTextureLocation(entity)), false, false);

        this.model.renderToBuffer(poseStack, builder, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, lightmap);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SpellProjectileEntity projectile) {
        return ResourceLocation.fromNamespaceAndPath("manaweave_and_runes","textures/entity/spell_projectile.png");
    }
}
