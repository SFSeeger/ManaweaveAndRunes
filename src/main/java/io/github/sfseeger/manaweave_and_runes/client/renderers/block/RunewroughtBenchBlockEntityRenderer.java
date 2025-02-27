package io.github.sfseeger.manaweave_and_runes.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RunewroughtBenchBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blocks.RunewroughtBenchBlock;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.neoforge.items.IItemHandler;

public class RunewroughtBenchBlockEntityRenderer implements BlockEntityRenderer<RunewroughtBenchBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public RunewroughtBenchBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super();
        this.context = ctx;
    }


    @Override
    public void render(RunewroughtBenchBlockEntity be, float v, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        BlockPos pos = be.getBlockPos();
        BlockPos posAbove = pos.above();

        IItemHandler itemHandler = be.getItemHandler(null);
        ItemStack stack = itemHandler.getStackInSlot(0);
        Level level = be.getLevel();
        Direction direction = be.getBlockState().getValue(RunewroughtBenchBlock.FACING);
        Direction direction1 = Direction.from2DDataValue((direction.get2DDataValue()) % 4);
        float f = -direction1.toYRot();


        if (level != null && !stack.isEmpty()) {
            packedLight = LightTexture.pack(level.getBrightness(LightLayer.BLOCK, posAbove),
                                            level.getBrightness(LightLayer.SKY, posAbove));

            poseStack.pushPose();

            poseStack.translate(0.5, 1f, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(f + 180));
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            this.context.getItemRenderer()
                    .renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack,
                                  multiBufferSource, level, 0);
            poseStack.popPose();
        }
    }
}
