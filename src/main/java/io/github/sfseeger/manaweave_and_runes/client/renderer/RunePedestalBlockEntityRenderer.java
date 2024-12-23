package io.github.sfseeger.manaweave_and_runes.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RunePedestalBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blocks.RunePedestalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class RunePedestalBlockEntityRenderer implements BlockEntityRenderer<RunePedestalBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public RunePedestalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(RunePedestalBlockEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        BlockPos pos = blockEntity.getBlockPos().above();
        IItemHandler itemHandler = blockEntity.getItemHandler(null);
        ItemStack stack = itemHandler.getStackInSlot(0);
        Level level = blockEntity.getLevel();
        Direction direction = blockEntity.getBlockState().getValue(RunePedestalBlock.FACING);

        if (level != null && !stack.isEmpty()) {
            packedLight = LightTexture.pack(
                    level.getBrightness(LightLayer.BLOCK, pos),
                    level.getBrightness(LightLayer.SKY, pos)
            );
            boolean isBlock = stack.getItem() instanceof BlockItem;
            float scalingFactor = isBlock ? 1f : .5f;


            poseStack.pushPose();
            poseStack.translate(0.5, 1.25f, 0.5);

            Direction direction1 = Direction.from2DDataValue((direction.get2DDataValue()) % 4);
            float f = -direction1.toYRot();
            poseStack.mulPose(Axis.YP.rotationDegrees(f));

            //poseStack.scale(scalingFactor, scalingFactor, scalingFactor);
            this.context.getItemRenderer()
                    .renderStatic(stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
                                  poseStack,
                                  multiBufferSource, level, 0);

            Font font = this.context.getFont();
            poseStack.scale(0.05f, -0.05f, 0.05f);
            HitResult result = Minecraft.getInstance().hitResult;


            // TODO: Find out how to check if the player is looking at the block & rotate accordingly
//            poseStack.translate(-font.width(stack.getCount() + "") / 2f, -15f - (isBlock ? 0 : 5), 0);
//            font.drawInBatch(stack.getCount() + "", 0, 0, 0xECECEC, false, poseStack.last().pose(),
//                             multiBufferSource,
//                             Font.DisplayMode.NORMAL, 0xFF0000, packedLight, true);
            poseStack.popPose();
        }
    }
}
