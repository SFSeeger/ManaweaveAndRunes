package io.github.sfseeger.lib.client.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.sfseeger.lib.client.RenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class AssetUtils {

    /*
     * From ActualAdditions (https://github.com/Ellpeck/ActuallyAdditions)
     */
    public static final int MAX_LIGHT_X = 0xF000F0;
    public static final int MAX_LIGHT_Y = 0xF000F0;

    public static void renderManaThreads(PoseStack matrixStack, MultiBufferSource buffer, float offX, float offY,
            float offZ,
            float yaw, float pitch, float length, float rotationTime, int color, float alpha, float beamWidth) {
        Level world = Minecraft.getInstance().level;
        int baseR = (int) (((color >> 16) & 0xFF) * alpha);
        int baseG = (int) (((color >> 8) & 0xFF) * alpha);
        int baseB = (int) ((color & 0xFF) * alpha);
        int a = 255;

        int lightmap = LightTexture.pack(MAX_LIGHT_X, MAX_LIGHT_Y);

        matrixStack.pushPose();

        VertexConsumer builder = buffer.getBuffer(RenderTypes.MANA_THREAD);
        matrixStack.translate(0.5f, 0.5f, 0.5f);
        matrixStack.translate(offX, offY, offZ);

        matrixStack.mulPose(Axis.YP.rotationDegrees(yaw));
        matrixStack.mulPose(Axis.XP.rotationDegrees(pitch));

        Matrix4f matrix = matrixStack.last().pose();

        // Get the camera position and direction
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraDir = new Vec3(camera.getLookVector());

        float time = (world.getGameTime() % rotationTime) / rotationTime;
        float resolution = 15;
        float interval = 3f;

        for (int j = 0; j < 2; j++) {
            int modifier = j == 0 ? 1 : -1;

            for (int i = 0; i < length * resolution; i++) {
                float t1 = i / (length * resolution);
                float t2 = (i + 1) / (length * resolution);

                Vec3 particle1 = calculateParticlePosition(t1, length, interval, time, beamWidth, modifier);
                Vec3 particle2 = calculateParticlePosition(t2, length, interval, time, beamWidth, modifier);


                Vec3 normal = particle2.subtract(particle1).cross(cameraDir).normalize();
                float size = 0.02f;

                builder.addVertex(matrix, (float) (particle1.x - size * normal.x()),
                                  (float) (particle1.y - size * normal.y()), (float) (particle1.z - size * normal.z()))
                        .setColor(baseR, baseG, baseB, a).setLight(lightmap);
                builder.addVertex(matrix, (float) (particle1.x + size * normal.x()),
                                  (float) (particle1.y + size * normal.y()), (float) (particle1.z + size * normal.z()))
                        .setColor(baseR, baseG, baseB, a).setLight(lightmap);
                builder.addVertex(matrix, (float) (particle2.x + size * normal.x()),
                                  (float) (particle2.y + size * normal.y()), (float) (particle2.z + size * normal.z()))
                        .setColor(baseR, baseG, baseB, a).setLight(lightmap);
                builder.addVertex(matrix, (float) (particle2.x - size * normal.x()),
                                  (float) (particle2.y - size * normal.y()), (float) (particle2.z - size * normal.z()))
                        .setColor(baseR, baseG, baseB, a).setLight(lightmap);
            }
        }
        matrixStack.popPose();
    }

    private static Vec3 calculateParticlePosition(float t, float length, float interval, float time, float beamWidth,
            int modifier) {
        double a = t * Math.PI * 4 * (length / interval) + time * Math.PI * 2;
        float x = (float) (modifier * Math.sin(a) * beamWidth * 2);
        float y = (float) (modifier * Math.cos(a) * beamWidth * 2);
        float z = -t * length;
        return new Vec3(x, y, z);
    }

    public static void renderManaThreads(PoseStack matrixStack, MultiBufferSource buffer, Vec3 startOffset,
            Vec3 endOffset,
            float rotationTime, int color, float alpha, float beamWidth) {
        Vec3 combined = endOffset.subtract(startOffset);

        double pitch =
                Math.toDegrees(Math.atan2(combined.y, Math.sqrt(combined.x * combined.x + combined.z * combined.z)));
        double yaw = Math.toDegrees(Math.atan2(-combined.z, combined.x));
        double length = combined.length();

        renderManaThreads(matrixStack, buffer, (float) startOffset.x, (float) startOffset.y, (float) startOffset.z,
                          (float) yaw, (float) pitch, (float) length, rotationTime, color, alpha, beamWidth);
    }
}
