package io.github.sfseeger.manaweave_and_runes.client.renderers.entity;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraProxyEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CameraProxyEntityRenderer extends EntityRenderer<CameraProxyEntity> {
    public CameraProxyEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CameraProxyEntity cameraProxyEntity) {
        return ManaweaveAndRunes.asResource("textures/entity/camera_proxy_entity.png");
    }
}
