package io.github.sfseeger.manaweave_and_runes.client.screens;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.menus.SpellDesignerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static io.github.sfseeger.manaweave_and_runes.core.util.ScreenUtil.isMouseInBounds;

@OnlyIn(Dist.CLIENT)
public class SpellDesignerScreen extends AbstractContainerScreen<SpellDesignerMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                    "textures/gui/container/spell_designer.png");
    private static final ResourceLocation TEXT_INPUT =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/spell_designer/text_field");
    private static final ResourceLocation TEXT_INPUT_ACTIVE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/spell_designer/text_field_active");
    private static final ResourceLocation CHISEL_BUTTON =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/spell_designer/chisel_button");
    private static final ResourceLocation CHISEL_BUTTON_ACTIVE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/spell_designer/chisel_button_active");

    private static final ResourceLocation CHISEL_SLOT_HINT =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/spell_designer/chisel_slot");

    private static final int GUI_WIDTH = 244;
    private static final int GUI_HEIGHT = 221;

    private EditBox name;


    public SpellDesignerScreen(SpellDesignerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        this.inventoryLabelY = GUI_HEIGHT - 96;
        this.inventoryLabelX = 7;
        this.titleLabelY = 6;
        this.titleLabelX = 7;

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;


        this.name = new EditBox(this.font, leftPos + 184, topPos + 10, 57, 12, Component.translatable("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setValue(this.menu.getName());
        this.addWidget(this.name);

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderButtons(guiGraphics, mouseX, mouseY, partialTick);
        this.renderFG(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int mouseX, int mouseY) {
        guiGraphics.blit(GUI_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (!this.menu.hasChisel()) {
            guiGraphics.blitSprite(CHISEL_SLOT_HINT, this.leftPos + 204, this.topPos + 90, 16, 16);
        }

        guiGraphics.blitSprite(TEXT_INPUT, this.leftPos + 180, this.topPos + 6, 61, 16);
        guiGraphics.blitSprite(CHISEL_BUTTON, this.leftPos + 181, this.topPos + 88, 20, 19);
    }

    public void renderFG(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.name.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation chiselButtonSprite = isMouseInBounds(leftPos + 181, topPos + 88, 20, 19, mouseX,
                mouseY) ? CHISEL_BUTTON_ACTIVE : CHISEL_BUTTON;
        guiGraphics.blitSprite(chiselButtonSprite, leftPos + 181, topPos + 88, 20, 19);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseInBounds(leftPos + 181, topPos + 88, 20, 19, (int) mouseX, (int) mouseY)) {
            this.menu.craft(this.minecraft.player, this.name.getValue());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }
}
