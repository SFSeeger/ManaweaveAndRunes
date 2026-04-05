package io.github.sfseeger.manaweave_and_runes.core.util;

import io.github.sfseeger.manaweave_and_runes.common.entity.scrying.CameraProxyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PlayerUtils {
    public static boolean isPlayerScrying(LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        if (player.level().isClientSide)
            return Minecraft.getInstance().cameraEntity instanceof CameraProxyEntity;
        else if (player instanceof ServerPlayer serverPlayer)
            return serverPlayer.getCamera()  instanceof CameraProxyEntity;
        else
            return false;

    }
}
