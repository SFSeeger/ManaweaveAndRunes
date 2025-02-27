package io.github.sfseeger.manaweave_and_runes.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.SpellDesignerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;

@OnlyIn(Dist.CLIENT)
public class SpellDesignerBlockEntityRenderer implements BlockEntityRenderer<SpellDesignerBlockEntity> {
    private static final float ROTATION_PERIOD = 125f;
    private static final double HORIZONTAL_OFFSET = 0.35;

    private BlockEntityRendererProvider.Context context;


    public SpellDesignerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(SpellDesignerBlockEntity be, float v, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        Level level = be.getLevel();
        BlockPos pos = be.getBlockPos().above();
        ItemStackHandler itemHandler = be.getItemHandler(null);
        //private static

        if (level != null) {
            packedLight = LightTexture.pack(level.getBrightness(LightLayer.BLOCK, pos),
                                            level.getBrightness(LightLayer.SKY, pos));
            float rot = (level.getGameTime() % ROTATION_PERIOD) * (360f / ROTATION_PERIOD);

            poseStack.pushPose();
            poseStack.translate(0.5, 1.05, 0.5);

            poseStack.scale(1.0F, 1.0F, 1.0F);

            ResourceLocation texture =
                    ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "block/spell_circle");
            TextureAtlasSprite sprite =
                    Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);


            VertexConsumer buffer = multiBufferSource.getBuffer(RenderType.cutout());

            float minU = sprite.getU0();
            float maxU = sprite.getU1();
            float minV = sprite.getV0();
            float maxV = sprite.getV1();

            float width = 1.0F;
            float height = 1.0F;

            buffer.addVertex(poseStack.last().pose(), -width / 2, 0, -height / 2)
                    .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                    .setUv(minU, minV)
                    .setOverlay(packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 1, 0);
            buffer.addVertex(poseStack.last().pose(), -width / 2, 0, height / 2)
                    .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                    .setUv(minU, maxV)
                    .setOverlay(packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 1, 0);
            buffer.addVertex(poseStack.last().pose(), width / 2, 0, height / 2)
                    .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                    .setUv(maxU, maxV)
                    .setOverlay(packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 1, 0);
            buffer.addVertex(poseStack.last().pose(), width / 2, 0, -height / 2)
                    .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                    .setUv(maxU, minV)
                    .setOverlay(packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 1, 0);

            poseStack.popPose();

            if (!itemHandler.getStackInSlot(SpellDesignerBlockEntity.MAIN_SLOT_INDEX).isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5, 1.1, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(rot));
                poseStack.scale(0.2F, 0.2F, 0.2F);
                context.getItemRenderer()
                        .renderStatic(itemHandler.getStackInSlot(SpellDesignerBlockEntity.MAIN_SLOT_INDEX),
                                      ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack,
                                      multiBufferSource, level, 0);
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.translate(0.5, 1.1, 0.5);
            for (int i = 0; i < 4; i++) {
                if (!itemHandler.getStackInSlot(i + 1).isEmpty()) {
                    poseStack.pushPose();
                    // Set specific positions for each item
                    switch (i) {
                        case 0 -> poseStack.translate(HORIZONTAL_OFFSET, 0, 0);
                        case 1 -> poseStack.translate(0, 0, HORIZONTAL_OFFSET);
                        case 2 -> poseStack.translate(-HORIZONTAL_OFFSET, 0, 0);
                        case 3 -> poseStack.translate(0, 0, -HORIZONTAL_OFFSET);
                    }
                    poseStack.mulPose(Axis.YP.rotationDegrees(rot));
                    poseStack.scale(0.2F, 0.2F, 0.2F);

                    context.getItemRenderer()
                            .renderStatic(itemHandler.getStackInSlot(i + 1), ItemDisplayContext.FIXED, packedLight,
                                          OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, level, 0);
                    poseStack.popPose();
                }
            }
            poseStack.popPose();
        }
    }
}
