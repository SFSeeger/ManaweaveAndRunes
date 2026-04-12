package io.github.sfseeger.manaweave_and_runes.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.sfseeger.lib.client.block_entity_renderers.GeckoManaNodeRenderer;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaGeneratorBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.blocks.ManaGeneratorBlock;
import io.github.sfseeger.manaweave_and_runes.common.blocks.RunePedestalBlock;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class ManaGeneratorBlockEntityRenderer extends GeckoManaNodeRenderer<ManaGeneratorBlockEntity> {
    private static final float ROTATION_PERIOD = 75f;
    private final BlockEntityRendererProvider.Context context;

    public ManaGeneratorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new DefaultedBlockGeoModel<>(ManaweaveAndRunes.asResource("mana_generator")));
        this.context = ctx;
    }

    @Override
    public void actuallyRender(PoseStack poseStack, ManaGeneratorBlockEntity animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        BlockPos pos = animatable.getBlockPos().north();
        Level level = animatable.getLevel();

        if (level != null) {
            packedLight = LightTexture.pack(level.getBrightness(LightLayer.BLOCK, pos),
                                            level.getBrightness(LightLayer.SKY, pos));
            renderItem(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick,
                             packedLight, packedOverlay, colour);
    }

    private void renderItem(ManaGeneratorBlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0, 0.4f, 0);
        Direction direction = animatable.getBlockState().getValue(ManaGeneratorBlock.FACING);
        Direction direction1 = Direction.from2DDataValue((direction.get2DDataValue()) % 4);
        for (int i = 0; i < ManaGeneratorBlockEntity.INPUTS.length; i++) {
            ItemStack stack = animatable.getItem(ManaGeneratorBlockEntity.INPUTS[i]);
            Level level = animatable.getLevel();

            if (level == null) return;

            float f = -direction1.toYRot();
            float rot = (level.getGameTime() % ROTATION_PERIOD) * (360f / ROTATION_PERIOD);
            poseStack.mulPose(Axis.YP.rotationDegrees(f + rot));
            poseStack.scale(0.65f, 0.65f, 0.65f);

            this.context.getItemRenderer()
                    .renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource,
                                  level, i);
        }
        poseStack.popPose();
    }
}
