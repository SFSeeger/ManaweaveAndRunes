package io.github.sfseeger.manaweave_and_runes.client.screens;

import io.github.sfseeger.lib.common.mana.Manas;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.menus.RunicLoomMenu;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Quaternionf;

import javax.annotation.ParametersAreNonnullByDefault;

import static io.github.sfseeger.manaweave_and_runes.core.util.ScreenUtil.isMouseInBounds;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RunicLoomScreen extends AbstractContainerScreen<RunicLoomMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "textures/gui/container/runic_loom.png");
    private static final ResourceLocation CHISEL_SLOT_HINT =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/spell_designer/chisel_slot");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 221;
    private static final int CELL_WIDTH = 15;
    private static final int CELL_HEIGHT = 10;


    public RunicLoomScreen(RunicLoomMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        this.inventoryLabelY = GUI_HEIGHT - 91;
        this.inventoryLabelX = 48;
        this.titleLabelY = 6;
        this.titleLabelX = 7;

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderButtons(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(GUI_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (!this.menu.hasChisel()) {
            guiGraphics.blitSprite(CHISEL_SLOT_HINT, this.leftPos + 17, this.topPos + 32, 16, 16);
        }
    }

    private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int offset = isMouseInBounds(leftPos + 14, topPos + 63, 20, 19, mouseX, mouseY) ? 20 : 0;
        guiGraphics.blit(GUI_TEXTURE, leftPos + 14, topPos + 63, offset, 237, 20, 19);


        for (int col = 0; col < RunicLoomMenu.RUNE_COLUMNS; col++) {
            for (int row = 0; row < RunicLoomMenu.RUNE_ROWS; row++) {
                int x = leftPos + 54 + col * CELL_WIDTH;
                int y = topPos + 22 + row * CELL_HEIGHT;
                if (menu.getRuneInSlot(col, row) != null) {
//                    guiGraphics.fill(x, y, x + CELL_WIDTH, y + CELL_HEIGHT, menu.getRuneInSlot(col, row).properties().getColor() + 0xFF000000);
                    guiGraphics.pose().pushPose();
                    {
                        for (int i = -1; i < 1; i++) {
                            for (int i2 = -1; i2 < 1; i2++) {
                                int colIdx = Math.max(col + i, 0);
                                int rowIdx = Math.max(row + i2, 0);

                                if (menu.getRuneInSlot(colIdx, rowIdx) == null) continue;
                                float angle = (float) Math.atan2(row - colIdx, col - rowIdx);
                                guiGraphics.pose()
                                        .rotateAround(new Quaternionf().rotateZ(angle),
                                                      x + CELL_WIDTH / 2f, y + CELL_HEIGHT / 2f, 0);
                                guiGraphics.hLine(x, x + CELL_WIDTH, y + CELL_HEIGHT / 2,
                                                  menu.getRuneInSlot(col, row).properties().getColor() + 0xFF000000);
                            }
                        }
                    }
                    guiGraphics.pose().popPose();
                }
                if (isMouseInBounds(x, y, CELL_WIDTH, CELL_HEIGHT, mouseX, mouseY)) {
                    guiGraphics.fill(x, y, x + CELL_WIDTH, y + CELL_HEIGHT, 0x60FFFFFF);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int col = 0; col < RunicLoomMenu.RUNE_COLUMNS; col++) {
            for (int row = 0; row < RunicLoomMenu.RUNE_ROWS; row++) {
                int x = leftPos + 54 + col * CELL_WIDTH;
                int y = topPos + 22 + row * CELL_HEIGHT;
                if (isMouseInBounds(x, y, CELL_WIDTH, CELL_HEIGHT, mouseX, mouseY) && button == 0) {
                    menu.setRuneInSlot(col, row, Manas.AirMana);
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
