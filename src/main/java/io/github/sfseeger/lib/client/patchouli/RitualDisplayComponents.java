package io.github.sfseeger.lib.client.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.rituals.Ritual;
import io.github.sfseeger.lib.common.rituals.RitualInput;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class RitualDisplayComponents implements ICustomComponent {
    private static final int[] squareSizes = {18, 36, 54};
    private static final ResourceLocation TICK_ICON =
            ResourceLocation.fromNamespaceAndPath("manaweave_and_runes", "textures/gui/book/clock.png");
    private static final ResourceLocation HOURGLASS_ICON =
            ResourceLocation.fromNamespaceAndPath("manaweave_and_runes", "textures/gui/book/hourglass.png");
    public float scale = 0.7F;
    public float pedestalScale = 0.5F;
    @SerializedName("texture_width")
    public int textureWidth = 32;
    @SerializedName("texture_height")
    public int textureHeight = 32;
    transient ResourceLocation pedestalResource;
    private transient int x, y;
    private transient Ritual ritualObject;
    private transient RitualInput input;
    private String ritual;
    @SerializedName("pedestal_icon")
    private String pedestalIcon = "manaweave_and_runes:textures/gui/book/pedestal.png";

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
        pedestalResource = ResourceLocation.tryParse(pedestalIcon);
    }

    @Override
    public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        if (input != null) {
            Font font = Minecraft.getInstance().font;

            graphics.pose().pushPose();
            graphics.pose().translate(x, y, 0);
            graphics.pose().scale(scale, scale, scale);
            graphics.setColor(1F, 1F, 1F, 1F);
            RenderSystem.enableBlend();

            mouseX = (int) ((mouseX - x)); // TODO: Fix scale not being applied to mouse position
            mouseY = (int) ((mouseY - y));

            int pedestalsToRender = ritualObject.getTier().ordinal() * 4;
            int i = 0;
            while (i < pedestalsToRender) {
                int squareIndex = i / 4;
                int cornerIndex = i % 4;
                int offsetX =
                        (cornerIndex == 0 || cornerIndex == 2) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
                int offsetY =
                        (cornerIndex == 0 || cornerIndex == 1) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
                renderPedestal(graphics, context, offsetX, offsetY + 12, mouseX, mouseY);
                i++;
            }

            i = 0;
            for (Ingredient ingredient : input.getInitialItemCost()) {
                int squareIndex = i / 4;
                int cornerIndex = i % 4;
                int offsetX =
                        (cornerIndex == 0 || cornerIndex == 2) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
                int offsetY =
                        (cornerIndex == 0 || cornerIndex == 1) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
                context.renderIngredient(graphics, offsetX, offsetY, mouseX, mouseY, ingredient);
                i++;
            }
            for (Ingredient ingredient : input.getTickItemCost()) {
                int squareIndex = i / 4;
                int cornerIndex = i % 4;
                int offsetX =
                        (cornerIndex == 0 || cornerIndex == 2) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
                int offsetY =
                        (cornerIndex == 0 || cornerIndex == 1) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
                context.renderIngredient(graphics, offsetX, offsetY, mouseX, mouseY, ingredient);
                graphics.blit(TICK_ICON, offsetX + 4, offsetY - 4, 0, 0, 16, 16, 16, 16);
                i++;
            }

            i = 0;
            for (Map.Entry<Mana, Integer> entry : input.getManaCost().entrySet()) {
                renderMana(graphics, context, -x + i % 4 * 35, y + 25 + i / 4 * 20, mouseX, mouseY,
                           entry.getKey(), entry.getValue());
                i++;
            }

            MutableComponent itemRate =
                    Component.translatable("patchouli.manaweave_and_runes.ritual.item_consumption_rate")
                            .append(": ")
                            .append(input.getItemRate() + " tick(s)");
            MutableComponent tickRate =
                    Component.translatable("patchouli.manaweave_and_runes.ritual.mana_consumption_rate")
                            .append(": ")
                            .append(input.getManaRate() + " tick(s)");

            drawCenteredString(graphics, font, itemRate, 0, y + 60, 0x333333, false);
            drawCenteredString(graphics, font, tickRate, 0, y + 70, 0x333333, false);

            graphics.blit(HOURGLASS_ICON, -5, -y + 5, 16, 16, 16, 16, 16, 16);
            String duration = ritualObject.getDuration() == -1 ? Component.translatable(
                    "patchouli.manaweave_and_runes.ritual.ritual_duration_infinite").getString() : Math.round(
                    (ritualObject.getDuration() / 20.0) * 100) / 100.0 + "s";
            graphics.drawString(font, duration, 10, -y + 4 + 5, 0x333333, false);
            if (context.isAreaHovered(mouseX, mouseY, 0, -y + 10, 20, 16)) {
                context.setHoverTooltipComponents(
                        List.of(Component.translatable("patchouli.manaweave_and_runes.ritual.ritual_duration"),
                                Component.literal(ritualObject.getDuration() == -1 ? Component.translatable(
                                                "patchouli.manaweave_and_runes.ritual.ritual_duration_infinite")
                                        .getString() : ritualObject.getDuration() + " ticks")
                        ));
            }

            graphics.pose().popPose();
        }
    }

    private void drawCenteredString(GuiGraphics graphics, Font font, Component text, int x, int y, int color,
            boolean shadow) {
        FormattedCharSequence formattedcharsequence = text.getVisualOrderText();
        graphics.drawString(font, formattedcharsequence, x - font.width(formattedcharsequence) / 2, y, color, shadow);
    }

    private void renderPedestal(GuiGraphics graphics, IComponentRenderContext context, int x, int y, int mouseX,
            int mouseY) {
        if (pedestalScale == 0F) {
            return;
        }

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(pedestalScale, pedestalScale, pedestalScale);
        graphics.setColor(1F, 1F, 1F, 1F);
        RenderSystem.enableBlend();
        graphics.blit(pedestalResource, 0, 0, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        graphics.pose().popPose();
    }

    private void renderMana(GuiGraphics graphics, IComponentRenderContext context, int x, int y, int mouseX,
            int mouseY, Mana mana, int amount) {
        graphics.pose().pushPose();
        graphics.blitSprite(mana.properties().getIcon().orElse(ResourceLocation.parse("")), x, y, 16, 16);
        graphics.drawString(Minecraft.getInstance().font, Integer.toString(amount), x + 18, y + 4, 0xFFFFFF);
        if (context.isAreaHovered(mouseX, mouseY, (int) (x * scale), (int) (y * scale), 20, 16)) {
            context.setHoverTooltipComponents(List.of(
                    Component.translatable(mana.getDescriptionId())
            ));
        }
        graphics.pose().popPose();
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup, HolderLookup.Provider registries) {
        ResourceLocation ritualId =
                ResourceLocation.tryParse(lookup.apply(IVariable.wrap(ritual, registries)).asString());

        ritualObject = ManaweaveAndRunesRegistries.RITUAL_REGISTRY.get(ritualId);
        input = ritualObject.getInput(Minecraft.getInstance().getConnection().getLevel())
                .orElseThrow(() -> new IllegalArgumentException("Cannot find Ritualinput"));
        pedestalIcon = lookup.apply(IVariable.wrap(pedestalIcon, registries)).asString();
    }
}
