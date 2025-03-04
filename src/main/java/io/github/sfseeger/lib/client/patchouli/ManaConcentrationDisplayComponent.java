package io.github.sfseeger.lib.client.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.common.recipes.mana_concentrator.ManaConcentratorRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ManaConcentrationDisplayComponent implements ICustomComponent {
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
    private transient ManaConcentratorRecipe recipeInstance;
    private String recipe;
    @SerializedName("pedestal_icon")
    private String pedestalIcon = "manaweave_and_runes:textures/gui/book/pedestal.png";

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
        pedestalResource = ResourceLocation.tryParse(pedestalIcon);
        //entry.addRelevantStack(builder, recipeInstance.result(), pageNum);

    }

    @Override
    public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        if (recipeInstance != null) {
            graphics.pose().pushPose();
            graphics.pose().translate(x, y, 0);
            graphics.pose().scale(scale, scale, scale);
            graphics.setColor(1F, 1F, 1F, 1F);
            RenderSystem.enableBlend();
            mouseX = (int) ((mouseX - x)); // TODO: Fix scale not being applied to mouse position
            mouseY = (int) ((mouseY - y));

            context.renderItemStack(graphics, 0, 8, mouseX, mouseY, recipeInstance.result());

            int pedestalsToRender = recipeInstance.tier().ordinal() * 4;
            List<Ingredient> ingredients = recipeInstance.getIngredients();

            int i = 0;
            while (i < pedestalsToRender) {
                int squareIndex = i / 4;
                int cornerIndex = i % 4;
                int offsetX =
                        (cornerIndex == 0 || cornerIndex == 2) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
                int offsetY =
                        (cornerIndex == 0 || cornerIndex == 1) ? -squareSizes[squareIndex] : squareSizes[squareIndex];
                renderPedestal(graphics, context, offsetX, offsetY + 12, mouseX, mouseY);
                if (ingredients.size() > i) {
                    context.renderIngredient(graphics, offsetX, offsetY, mouseX, mouseY, ingredients.get(i));
                }
                i++;
            }

            i = 0;
            for (Map.Entry<Mana, Integer> entry : recipeInstance.manaMap().entrySet()) {
                renderMana(graphics, context, -x + i % 4 * 35, y + 20 + i / 4 * 20, mouseX, mouseY,
                           entry.getKey(), entry.getValue());
                i++;
            }

            graphics.blit(HOURGLASS_ICON, -5, -y + 5, 16, 16, 16, 16, 16, 16);
            graphics.drawString(Minecraft.getInstance().font,
                                Math.round((recipeInstance.craftTime() / 20.0) * 100) / 100.0 + "s", 10, -y + 4 + 5,
                                0xFFFFFF);
            if (context.isAreaHovered(mouseX, mouseY, 0, -y + 10, 20, 16)) {
                context.setHoverTooltipComponents(
                        List.of(Component.translatable("patchouli.manaweave_and_runes.mana_concentration.time")));
            }
            graphics.pose().popPose();
        }
    }

    private void renderPedestal(GuiGraphics graphics, IComponentRenderContext context, int x, int y, int mouseX,
            int mouseY) {
        if (pedestalScale == 0F) {
            return;
        }

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(pedestalScale, pedestalScale, pedestalScale);
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
        ResourceLocation recipeId =
                ResourceLocation.tryParse(lookup.apply(IVariable.wrap(recipe, registries)).asString());

        Recipe<?> recipeHolder = Minecraft.getInstance()
                .getConnection()
                .getRecipeManager()
                .byKey(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Recipe ID"))
                .value();
        if (!(recipeHolder instanceof ManaConcentratorRecipe))
            throw new IllegalArgumentException("Invalid Recipe Type");
        recipeInstance = (ManaConcentratorRecipe) recipeHolder;
        pedestalIcon = lookup.apply(IVariable.wrap(pedestalIcon, registries)).asString();
    }
}
