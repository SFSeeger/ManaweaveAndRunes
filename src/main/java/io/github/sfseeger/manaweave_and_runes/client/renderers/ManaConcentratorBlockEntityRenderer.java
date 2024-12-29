package io.github.sfseeger.manaweave_and_runes.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaConcentratorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blocks.mana_concentrator.ManaConcentratorType;
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
import net.neoforged.neoforge.items.IItemHandler;

public class ManaConcentratorBlockEntityRenderer implements BlockEntityRenderer<ManaConcentratorBlockEntity> {
    private static final float ROTATION_PERIOD = 150f;
    private final BlockEntityRendererProvider.Context context;

    public ManaConcentratorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(ManaConcentratorBlockEntity be, float partialTick, PoseStack poseStack,
            MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        BlockPos pos = be.getBlockPos().offset(-1, 0, 0);
        IItemHandler itemHandler = be.getItemHandler(null);
        Level level = be.getLevel();

        if (level != null) {
            packedLight = LightTexture.pack(
                    level.getBrightness(LightLayer.BLOCK, pos),
                    level.getBrightness(LightLayer.SKY, pos)
            );
            int fullSlots = fullSlots(itemHandler);
            float rot = (level.getGameTime() % ROTATION_PERIOD) * (360f / ROTATION_PERIOD);
            float rotationPerItem = 360f / fullSlots;

            ManaConcentratorType type = be.getManaConcentratorType();


            poseStack.pushPose();
            poseStack.translate(0.5, be.getEffectYOffset(), 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(rot));
            for (int i = 0; i < fullSlots; i++) {
                poseStack.mulPose(Axis.YP.rotationDegrees(rotationPerItem));
                poseStack.translate(0, 0, 1);
                ItemStack stack = itemHandler.getStackInSlot(i);
                this.context.getItemRenderer()
                        .renderStatic(stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
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
