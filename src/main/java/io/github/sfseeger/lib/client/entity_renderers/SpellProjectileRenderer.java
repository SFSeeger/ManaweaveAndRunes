package io.github.sfseeger.lib.client.entity_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.sfseeger.lib.client.RenderTypes;
import io.github.sfseeger.lib.common.entities.projectiles.SpellProjectileEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class SpellProjectileRenderer extends EntityRenderer<SpellProjectileEntity> {
    public SpellProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SpellProjectileEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight) {
        int lightmap = LightTexture.pack(0xF000F0, 0xF000F0);
        poseStack.pushPose();
        VertexConsumer builder = bufferSource.getBuffer(RenderTypes.MANA_THREAD);

        Matrix4f matrix = poseStack.last().pose();


        builder.addVertex(matrix, 0f, 0f, 0f).setColor(255, 255, 255, 255).setLight(lightmap);
        builder.addVertex(matrix, 0f, 0f, 1f).setColor(255, 255, 255, 255).setLight(lightmap);
        builder.addVertex(matrix, 1f, 0f, 1f).setColor(255, 255, 255, 255).setLight(lightmap);
        builder.addVertex(matrix, 1f, 0f, 0f).setColor(255, 255, 255, 255).setLight(lightmap);

        builder.addVertex(matrix, 0f, 1f, 0f).setColor(255, 255, 255, 255).setLight(lightmap);
        builder.addVertex(matrix, 0f, 1f, 1f).setColor(255, 255, 255, 255).setLight(lightmap);
        builder.addVertex(matrix, 1f, 1f, 1f).setColor(255, 255, 255, 255).setLight(lightmap);
        builder.addVertex(matrix, 1f, 1f, 0f).setColor(255, 255, 255, 255).setLight(lightmap);

        poseStack.popPose();

    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SpellProjectileEntity projectile) {
        return ResourceLocation.withDefaultNamespace("textures/item/baked_potato.png");
    }
}
