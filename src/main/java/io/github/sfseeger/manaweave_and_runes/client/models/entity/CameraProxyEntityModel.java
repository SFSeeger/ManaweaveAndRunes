package io.github.sfseeger.manaweave_and_runes.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraProxyEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class CameraProxyEntityRenderer extends EntityModel<CameraProxyEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation
			LAYER_LOCATION = new ModelLayerLocation(ManaweaveAndRunes.asResource("camera_proxy_entity"), "main");
	private final ModelPart root;


	public CameraProxyEntityRenderer(ModelPart root) {
		this.root = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		

		return LayerDefinition.create(meshdefinition, 16, 16);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {

	}

	@Override
	public void setupAnim(CameraProxyEntity cameraProxyEntity, float v, float v1, float v2, float v3, float v4) {

	}
}