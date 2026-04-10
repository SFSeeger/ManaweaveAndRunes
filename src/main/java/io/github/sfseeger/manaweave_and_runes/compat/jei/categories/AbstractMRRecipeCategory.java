package io.github.sfseeger.manaweave_and_runes.compat.jei.categories;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.core.util.ScreenUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractMRRecipeCategory<T> implements IRecipeCategory<T> {
    private static final ResourceLocation PEDESTAL_ICON = ResourceLocation.fromNamespaceAndPath("manaweave_and_runes",
                                                                                                "textures/gui/book/pedestal.png");
    protected final IGuiHelper guiHelper;
    private final Component localizedName;
    private final IDrawable icon;
    private final int width;
    private final int height;
    // graphics
    private final IDrawableStatic slotDrawable;
    private final IDrawableStatic arrowDrawable;
    private final IDrawableStatic arrowFilledDrawable;
    private final IDrawableStatic pedestalIconDrawable;


    public AbstractMRRecipeCategory(IGuiHelper guiHelper, Info info) {
        this.guiHelper = guiHelper;
        localizedName = Component.translatable(info.translationKey());
        ItemStack renderStack = new ItemStack(info.icon());
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, renderStack.copy());
        width = info.width;
        height = info.height;

        slotDrawable = guiHelper.getSlotDrawable();
        arrowDrawable = guiHelper.getRecipeArrow();
        arrowFilledDrawable = guiHelper.getRecipeArrowFilled();
        pedestalIconDrawable = guiHelper.drawableBuilder(PEDESTAL_ICON, 0, 0, 32, 32)
                .setTextureSize(32, 32)
                .build();
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    // Drawing methods
    protected void addSlot(GuiGraphics guiGraphics, int x, int y) {
        slotDrawable.draw(guiGraphics, x, y);
    }

    protected void addArrow(GuiGraphics guiGraphics, int x, int y) {
        addArrow(guiGraphics, x, y, true);
    }

    protected void addArrow(GuiGraphics guiGraphics, int x, int y, boolean filled) {
        if (filled) {
            arrowFilledDrawable.draw(guiGraphics, x, y);
        } else {
            arrowDrawable.draw(guiGraphics, x, y);
        }
    }

    protected void addPedestal(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x - 5, y + 12, 0);
        guiGraphics.pose().scale(0.6f, 0.6f, 1);
        pedestalIconDrawable.draw(guiGraphics, 0, 0);
        guiGraphics.pose().popPose();
    }

    protected void addMana(GuiGraphics guiGraphics, int x, int y, double mouseX, double mouseY, Mana mana, int amount) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(0.7f, 0.7f, 1);
        guiGraphics.blitSprite(mana.properties().getIcon().orElse(ResourceLocation.parse("")), 0, 0, 16, 16);

        Component ammountComponent = Component.literal(Integer.toString(amount));

        guiGraphics.drawString(Minecraft.getInstance().font, ammountComponent,  18, 4, 0xFFFFFF);
        if (ScreenUtil.isMouseInBounds(x, y, 16 + 18 + Integer.toString(amount).length() * 2, 14, mouseX, mouseY)) {
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, List.of(mana.getName()), (int) (mouseX - x),
                                               (int) (mouseY - y));
        }
        guiGraphics.pose().popPose();
    }

    public record Info(String translationKey, ItemLike icon, int width, int height) {
    }
}
