package io.github.sfseeger.manaweave_and_runes.client.screens;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.menus.RunewroughtBenchMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RunewroughtBenchScreen extends AbstractContainerScreen<RunewroughtBenchMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                  "textures/gui/container/runewrought_bench.png");
    private static final ResourceLocation CROSS =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/runewrought_bench/cross");


    private int uiX;
    private int uiY;


    public RunewroughtBenchScreen(RunewroughtBenchMenu menu,
            Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
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
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int mouseX, int mouseY) {
        guiGraphics.blit(GUI_TEXTURE, this.uiX, this.uiY, 0, 0, this.imageWidth, this.imageHeight);

        if (!this.menu.hasUpgradable()) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    guiGraphics.blitSprite(CROSS, this.uiX + 53 + j * 18, this.uiY + 15 + i * 18, 16, 16);
                }
            }
        } else {
            int slots = this.menu.getSlotAmount();
            for (int i = slots; i < 12; i++) {
                guiGraphics.blitSprite(CROSS, this.uiX + 53 + (i % 4) * 18, this.uiY + 15 + (i / 4) * 18, 16, 16);
            }
        }
    }
}
