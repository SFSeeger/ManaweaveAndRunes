package io.github.sfseeger.manaweave_and_runes.client.screens.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.spells.ISpellCaster;
import io.github.sfseeger.lib.common.spells.Spell;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.core.init.MRItemInit;
import io.github.sfseeger.manaweave_and_runes.core.util.ManaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Quaternionf;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class ManaweaversStaffGui {
    private static final ResourceLocation MANA_INDICATOR = ManaweaveAndRunes.asResource("mana_indicator");


    public static void render(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) return;

        ItemStack heldItem = player.getMainHandItem();

        GuiGraphics gui = event.getGuiGraphics();
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        int hotbarX = (screenWidth - 182) / 2;
        int hotbarY = screenHeight - 22;

        boolean holdsCaster = heldItem.getItem() instanceof ISpellCaster;

        if (holdsCaster) {
            Spell currentSpell = ((ISpellCaster) heldItem.getItem()).getCurrrntSpell(heldItem);
            if (currentSpell != null) {
                gui.drawString(minecraft.font, currentSpell.getName(), hotbarX - 120, hotbarY + 6, 0xFFFFFF);
            }
        }

        Map<Mana, Integer> manaAvailable = ManaUtils.getManaAvailable(player);
        Map<Mana, Integer> manaTotal = ManaUtils.getManaTotal(player);
        List<Mana> storedMana = manaTotal.keySet()
                .stream()
                .sorted(Comparator.comparing(Mana::getDescriptionId))
                .toList()
                .subList(0, Math.min(8, manaTotal.size()));

        if (!storedMana.isEmpty() || holdsCaster) {
            Map<Mana, Integer> manaCost = Map.of();
            if (holdsCaster) {
                manaCost = ((ISpellCaster) heldItem.getItem()).getCurrrntSpell(heldItem).getManaCost();
            }

            RenderSystem.enableBlend();

            gui.pose().pushPose();
            gui.pose().translate(5, 5, 0);
            gui.pose().scale(0.5f, 0.5f, 1);

            gui.pose().pushPose();
            gui.blitSprite(MANA_INDICATOR, 256, 256, 0, 128, 0, 0, 128, 128);
            gui.pose().popPose();

            int radius = 18 * 2;
            int centerX = 63;
            int centerY = 64;

            for (int i = 0; i < 8; i++) {
                int textureHeight = 48;
                gui.pose().pushPose();
                float angle = (float) Math.toRadians(i * 45);
                gui.pose().translate(centerX - (textureHeight - 1) / 2f, centerY - textureHeight / 2f, 0);
                gui.pose().rotateAround(new Quaternionf().rotateZ(angle), textureHeight / 2f, textureHeight / 2f, 0);
                gui.pose().translate(0, -radius, 0);

                // Mana Indicator
                float fill = 0f;
                int color = 0xFF_FF_FF;
                float cost = 0f;
                if (storedMana.size() > i) {
                    Mana currentMana = storedMana.get(i);
                    fill = (float) manaAvailable.getOrDefault(currentMana, 0) / manaTotal.get(currentMana);
                    color = currentMana.properties().getColor();

                    cost = (float) manaCost.getOrDefault(currentMana, 0) / manaTotal.get(currentMana);
                    cost = cost > 0 ? Math.max(cost, 0.05f) : 0f;
                }

                int fillOffset = (int) (textureHeight * fill);
                int inverseFillOffset = textureHeight - fillOffset;
                int costOffset = (int) (textureHeight * cost);

                gui.pose().translate(0, inverseFillOffset, 0);
                gui.setColor(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, (color & 0xFF) / 255f, 1f);
                gui.blitSprite(MANA_INDICATOR, 256, 256, 128 + 48, 48 + inverseFillOffset, 0, 0, 48, fillOffset);
                gui.setColor(1, 1, 1, 1f);
                gui.pose().translate(0, -(inverseFillOffset), 0);

                int playerTicks = player.tickCount;
                if (costOffset > 0) {
                    gui.pose().translate(0, inverseFillOffset, 0);
                    float blinkAlpha = (float)Math.sin(playerTicks * Math.PI / 15) * 0.5f + 0.5f;
                    gui.setColor(0.8f, 0, 0, blinkAlpha);
                    gui.blitSprite(MANA_INDICATOR, 256, 256, 128 + 48, 48 + inverseFillOffset, 0, 0, 48, costOffset);
                    gui.setColor(1, 1, 1, 1f);
                    gui.pose().translate(0, -inverseFillOffset, 0);
                }

                // Mana Container
                gui.blitSprite(MANA_INDICATOR, 256, 256, 128, 48, 0, 0, 48, 48);
                gui.pose().popPose();
            }

            gui.pose().pushPose();
            gui.pose().translate(0, 0, 0.5f);
            gui.blitSprite(MANA_INDICATOR, 256, 256, 0, 0, 0, 0, 128, 128);
            gui.pose().popPose();

            gui.pose().pushPose();
            gui.renderItem(MRItemInit.SPELL_HOLDER_ITEM.toStack(), 56, 56);
            gui.pose().popPose();

            gui.pose().popPose();

            RenderSystem.disableBlend();
        }
    }
}
