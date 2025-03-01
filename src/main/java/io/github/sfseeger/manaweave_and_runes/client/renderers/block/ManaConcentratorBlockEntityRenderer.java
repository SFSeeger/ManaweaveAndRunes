package io.github.sfseeger.manaweave_and_runes.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaConcentratorBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ManaConcentratorBlockEntityRenderer extends GeoBlockRenderer<ManaConcentratorBlockEntity> {
    private static final float ROTATION_PERIOD = 150f;
    private final BlockEntityRendererProvider.Context context;

    public ManaConcentratorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new DefaultedBlockGeoModel<>(
                ResourceLocation.fromNamespaceAndPath("manaweave_and_runes", "mana_concentrator")));
        this.context = ctx;
    }

    @Override
    public void actuallyRender(PoseStack poseStack, ManaConcentratorBlockEntity animatable, BakedGeoModel model,
            @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer,
            boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        BlockPos pos = animatable.getBlockPos().north();
        Level level = animatable.getLevel();

        if (level != null) {
            packedLight = LightTexture.pack(
                    level.getBrightness(LightLayer.BLOCK, pos),
                    level.getBrightness(LightLayer.SKY, pos)
            );
            renderRunes(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick,
                             packedLight, packedOverlay, colour);
    }

    public void renderRunes(ManaConcentratorBlockEntity be, float partialTick, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        IItemHandler itemHandler = be.getItemHandler(null);
        Level level = be.getLevel();

        if (level != null) {
            int fullSlots = fullSlots(itemHandler);
            float rot = (level.getGameTime() % ROTATION_PERIOD) * (360f / ROTATION_PERIOD);
            float rotationPerItem = 360f / fullSlots;

            poseStack.pushPose();
            poseStack.translate(0, be.getEffectYOffset(), 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(rot));
            for (int i = 0; i < fullSlots; i++) {
                poseStack.mulPose(Axis.YP.rotationDegrees(rotationPerItem));
                poseStack.translate(0, 0, 1);
                ItemStack stack = itemHandler.getStackInSlot(i);
                this.context.getItemRenderer()
                        .renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay,
                                      poseStack, multiBufferSource, level, i);
                poseStack.translate(0, 0, -1);
            }
            poseStack.popPose();
        }
    }

    private int fullSlots(IItemHandler handler) {
        return (int) java.util.stream.IntStream.range(0, handler.getSlots())
                .filter(i -> !handler.getStackInSlot(i).isEmpty())
                .count();
    }
}
