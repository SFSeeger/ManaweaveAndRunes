package io.github.sfseeger.manaweave_and_runes.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.sfseeger.lib.client.block_entity_renderers.ManaNodeRenderer;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.RunePedestalBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blocks.RunePedestalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class RunePedestalBlockEntityRenderer extends ManaNodeRenderer<RunePedestalBlockEntity> {

    private final BlockEntityRendererProvider.Context context;
    private static final float ROTATION_PERIOD = 150f;

    public RunePedestalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
        this.context = ctx;
    }

    @Override
    public void render(RunePedestalBlockEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        Vec3 pos = blockEntity.getBlockPos().getCenter();
        BlockPos posAbove = blockEntity.getBlockPos().above();
        IItemHandler itemHandler = blockEntity.getItemHandler(null);
        ItemStack stack = itemHandler.getStackInSlot(0);
        Level level = blockEntity.getLevel();
        Direction direction = blockEntity.getBlockState().getValue(RunePedestalBlock.FACING);

        if (level != null && !stack.isEmpty()) {
            packedLight = LightTexture.pack(
                    level.getBrightness(LightLayer.BLOCK, posAbove),
                    level.getBrightness(LightLayer.SKY, posAbove)
            );
            boolean isBlock = stack.getItem() instanceof BlockItem;


            poseStack.pushPose();
            poseStack.translate(0.5, 1.25f, 0.5);

            Direction direction1 = Direction.from2DDataValue((direction.get2DDataValue()) % 4);
            float f = -direction1.toYRot();
            float rot = (level.getGameTime() % ROTATION_PERIOD) * (360f / ROTATION_PERIOD);
            poseStack.mulPose(Axis.YP.rotationDegrees(f + rot));

            //for (int i = 0; i < itemCount; i++) {
            //poseStack.translate(i * 0.02f, 0, -i * 0.02f);
            this.context.getItemRenderer()
                    .renderStatic(stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
                                  poseStack,
                                  multiBufferSource, level, 0);
            //}

            poseStack.popPose();

            poseStack.pushPose();
            Font font = this.context.getFont();
            poseStack.translate(0.5f, 1.25f, .5f);
            poseStack.scale(0.05f, -0.05f, 0.05f);
            HitResult result = Minecraft.getInstance().hitResult;

            if (result != null && result.getType() == HitResult.Type.BLOCK && result.getLocation()
                    .distanceTo(pos) <= 0.8) {
                poseStack.mulPose(Axis.YP.rotationDegrees(180 - Minecraft.getInstance().getCameraEntity().getYRot()));
                poseStack.translate(-font.width(stack.getCount() + "") / 2f, -15f,
                                    -font.width(stack.getCount() + "") / 2f);
                font.drawInBatch(stack.getCount() + "", 0, 0, 0xECECEC, false, poseStack.last().pose(),
                                 multiBufferSource,
                                 Font.DisplayMode.NORMAL, 0xFF0000, packedLight, true);
            }

            poseStack.popPose();


        }
        super.render(blockEntity, partialTick, poseStack, multiBufferSource, packedLight, packedOverlay);
    }
}
