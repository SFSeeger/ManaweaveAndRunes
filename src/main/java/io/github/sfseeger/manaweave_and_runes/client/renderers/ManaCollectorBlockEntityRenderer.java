package io.github.sfseeger.manaweave_and_runes.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.sfseeger.lib.client.ber.ManaNodeRenderer;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaCollectorBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class ManaCollectorBlockEntityRenderer extends ManaNodeRenderer<ManaCollectorBlockEntity> {
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

            poseStack.pushPose();

            poseStack.translate(0.5, 1.01f, 0.5);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            this.context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, level, 0);
            poseStack.popPose();
        }
    }
}
