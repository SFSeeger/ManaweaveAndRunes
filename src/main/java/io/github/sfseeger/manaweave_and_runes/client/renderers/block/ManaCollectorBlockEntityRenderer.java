package io.github.sfseeger.manaweave_and_runes.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.sfseeger.lib.client.block_entity_renderers.ManaNodeRenderer;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaCollectorBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class ManaCollectorBlockEntityRenderer extends ManaNodeRenderer<ManaCollectorBlockEntity> {
    private static final float ROTATION_PERIOD = 75f;
    private BlockEntityRendererProvider.Context context;

    public ManaCollectorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
        this.context = ctx;
    }

    @Override
    public void render(ManaCollectorBlockEntity manaCollectorBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        super.render(manaCollectorBlockEntity, partialTick, poseStack, multiBufferSource, packedLight, packedOverlay);
        BlockPos pos = manaCollectorBlockEntity.getBlockPos();
        BlockPos posAbove = pos.above();
        IItemHandler itemHandler = manaCollectorBlockEntity.getItemHandler(null);
        ItemStack stack = itemHandler.getStackInSlot(0);
        Level level = manaCollectorBlockEntity.getLevel();


        if (level != null && !stack.isEmpty()) {
            packedLight = LightTexture.pack(level.getBrightness(LightLayer.BLOCK, posAbove), level.getBrightness(LightLayer.SKY, posAbove));
            float rot = (level.getGameTime() % ROTATION_PERIOD) * (360f / ROTATION_PERIOD);

            poseStack.pushPose();

            poseStack.translate(0.5, 1.1f, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(rot));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            this.context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, level, 0);
            poseStack.popPose();
        }
    }
}
