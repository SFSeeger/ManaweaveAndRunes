package io.github.sfseeger.manaweave_and_runes.client.renderers.item.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.sfseeger.lib.common.mana.Mana;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManaDisplayTooltipComponent implements ClientTooltipComponent, TooltipComponent {
    public static final int ICON_SIZE = 8;
    public static final int ICON_PADDING = 2;
    public static final int ICON_SPACING = 15;
    private static final int MAX_PER_LINE = 10;
    private static final int MAX_LINES = 3;
    private static final int MAX_MANA = MAX_LINES * MAX_PER_LINE;
    private final Map<Mana, Integer> manaCost;
    private final List<Mana> sortedMana;

    public ManaDisplayTooltipComponent(Map<Mana, Integer> manaCost) {
        this.manaCost = manaCost;
        sortedMana = manaCost.keySet().stream().sorted(Comparator.comparing(Mana::getDescriptionId)).toList();
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if (sortedMana.size() > MAX_MANA) {
            drawMana(guiGraphics, font, x, y, MAX_MANA);
            int overhead = sortedMana.size() - MAX_MANA;
            guiGraphics.drawString(font, "+ " + overhead, x, y, 0xFF0000);
        } else {
            drawMana(guiGraphics, font, x, y, sortedMana.size());
        }
    }

    private void drawMana(GuiGraphics guiGraphics, Font font, int x, int y, int maxIngredients) {
        final int maxPerLine = Math.ceilDiv(maxIngredients, getLineCount());

        for (int i = 0; i < sortedMana.size() && i < maxIngredients; i++) {
            int column = i % maxPerLine;
            int row = i / maxPerLine;
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            {
                poseStack.translate(
                        x + column * (ICON_SIZE + ICON_SPACING) + ICON_PADDING,
                        y + row * ICON_SIZE + ICON_PADDING,
                        0.0D
                );
                poseStack.scale(0.6F, 0.6F, 1.0F);
                guiGraphics.blitSprite(
                        sortedMana.get(i)
                                .properties()
                                .getIcon()
                                .orElse(ResourceLocation.withDefaultNamespace("missing")), 0, 0,
                        16, 16);
                guiGraphics.drawString(font,
                                       String.valueOf(manaCost.get(sortedMana.get(i))),
                                       2 * ICON_SIZE + ICON_PADDING, 5, 0xFFFFFF);
            }
            poseStack.popPose();
        }
    }

    private int getLineCount() {
        int lineCount = Math.ceilDiv(manaCost.size(), MAX_PER_LINE);
        return Math.min(lineCount, MAX_LINES);
    }

    private int getMaxPerLine() {
        int perLine = Math.ceilDiv(manaCost.size(), getLineCount());
        return Math.min(perLine, MAX_PER_LINE);
    }

    @Override
    public int getHeight() {
        return getLineCount() * ICON_SIZE + 2 * ICON_PADDING;
    }

    @Override
    public int getWidth(Font font) {
        return getMaxPerLine() * (ICON_SIZE + ICON_SPACING) + 2 * ICON_PADDING;
    }

}
