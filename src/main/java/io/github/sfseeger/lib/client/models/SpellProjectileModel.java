package io.github.sfseeger.lib.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.sfseeger.lib.common.entities.projectiles.SpellProjectileEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SpellProjectileModel extends EntityModel<SpellProjectileEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("manaweave_and_runes", "spell_projectile"), "main");
    private final ModelPart root;

    public SpellProjectileModel(ModelPart root){
        this.root = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create()
                                                                       .texOffs(0, 19)
                                                                       .addBox(-2.0F, -4.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                                                                       .texOffs(0, 14)
                                                                       .addBox(-3.0F, -3.0F, -1.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -2.0F, -2.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                                                                       .texOffs(8, 19)
                                                                       .addBox(-2.0F, 1.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                                                                       .texOffs(16, 14)
                                                                       .addBox(-3.0F, 0.0F, -1.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                                                                       .texOffs(0, 7)
                                                                       .addBox(-4.0F, -1.0F, -2.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)),
                                                               PartPose.offset(1.0F, 19.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(SpellProjectileEntity spellProjectileEntity, float v, float v1, float v2, float v3, float v4) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        root.render(poseStack, vertexConsumer, i, i1, i2);
    }
}
