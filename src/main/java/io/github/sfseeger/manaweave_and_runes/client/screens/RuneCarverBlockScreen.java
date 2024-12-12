package io.github.sfseeger.manaweave_and_runes.client.screens;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.menus.RuneCarverBlockMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RuneCarverBlockScreen extends AbstractContainerScreen<RuneCarverBlockMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                  "textures/gui/container/rune_carver_block.png");

    private static final ResourceLocation BUTTON_ACTIVE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/button_active");
    private static final ResourceLocation BUTTON_INACTIVE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/button_inactive");
    private static final ResourceLocation BUTTON_SELECTED =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/button_selected");
    private static final ResourceLocation CHISEL_BUTTON_ACTIVE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                  "container/rune_carver/chisel_button_active");
    private static final ResourceLocation CHISEL_BUTTON_INACTIVE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                  "container/rune_carver/chisel_button_inactive");
    private static final ResourceLocation CHISEL_SLOT_HINT =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/chisel_slot_hint");
    private static final ResourceLocation RUNE_SLOT_HINT =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/rune_slot_hint");
    private static final ResourceLocation SCROLL_ACTIVE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/scroll_active");
    private static final ResourceLocation SCROLL_INACTIVE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/scroll_inactive");

    private int uiX;
    private int uiY;
    private boolean displayRecipes;
    private float scrollOffs;
    private int startIndex;

    private static final int SELECTOR_X = 49;
    private static final int SELECTOR_Y = 15;

    public RuneCarverBlockScreen(RuneCarverBlockMenu menu, Inventory playerInventory,
            Component title) {
        super(menu, playerInventory, title);
        menu.registerUpdateListener(this::containerChanged);
    }

    @Override
    protected void init() {
        super.init();
        this.uiX = (this.width - this.imageWidth) / 2;
        this.uiY = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        this.renderButtons(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int mouseX, int mouseY) {
        guiGraphics.blit(GUI_TEXTURE, uiX, uiY, 0, 0, this.imageWidth, this.imageHeight);

        if (!menu.getChiselSlot().hasItem()) {
            guiGraphics.blitSprite(CHISEL_SLOT_HINT, uiX + 20, uiY + 15, 16, 16);
        }
        if (!this.menu.getRuneSlot().hasItem()) {
            guiGraphics.blitSprite(RUNE_SLOT_HINT, uiX + 20, uiY + 53, 16, 16);
        }

        int scrollYOffset = (int) (41.0F * this.scrollOffs);
        ResourceLocation scroll = displayRecipes ? SCROLL_ACTIVE : SCROLL_INACTIVE;
        guiGraphics.blitSprite(scroll, uiX + 116, uiY + 15 + scrollYOffset, 12, 15);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (displayRecipes) {
            int selectorX = uiX + SELECTOR_X;
            int selectorY = uiY + SELECTOR_Y;
            if (isMouseInBounds(selectorX, selectorY, 66, 56, (int) mouseX, (int) mouseY)) {
                for (int i = 0; i < 12; i++) {
                    int x = selectorX + i * 18;
                    int y = selectorY + (i / 4) * 18;
                    if (isMouseInBounds(x, y, 16, 18, (int) mouseX, (int) mouseY)) {
                        this.menu.clickMenuButton(this.minecraft.player, i);
                        return true;
                    }
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void removed() {
        super.removed();
        this.menu.removed(minecraft.player);
    }

    protected void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        if (displayRecipes) {
            for (int i = 0; i < this.menu.getRecipes().size(); i++) {
                int x = uiX + SELECTOR_X + i * 17;
                int y = uiY + SELECTOR_Y + (i / 4) * 18;
                renderRecipeButton(guiGraphics, x, y, isMouseInBounds(x, y, 16, 18, mouseX, mouseY),
                                   i == this.menu.getSelectedRecipeIndex(), i);
            }
        }
    }


    protected void renderRecipeButton(GuiGraphics guiGraphics, int x, int y, boolean active, boolean selected,
            int recipeIndex) {
        if (selected) {
            guiGraphics.blitSprite(BUTTON_SELECTED, x, y, 16, 18);
        } else if (active) {
            guiGraphics.blitSprite(BUTTON_ACTIVE, x, y, 16, 18);
        } else {
            guiGraphics.blitSprite(BUTTON_INACTIVE, x, y, 16, 18);
        }
        guiGraphics.renderItem(
                this.menu.getRecipes().get(recipeIndex).value().getResultItem(this.minecraft.level.registryAccess()), x,
                y + 1);
    }

    private boolean isMouseInBounds(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private void containerChanged() {
        this.displayRecipes = this.menu.hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
    }

}
