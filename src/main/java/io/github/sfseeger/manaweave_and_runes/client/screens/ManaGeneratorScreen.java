package io.github.sfseeger.manaweave_and_runes.client.screens;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.menus.ManaGeneratorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class ManaGeneratorScreen extends AbstractContainerScreen<ManaGeneratorMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                  "textures/gui/container/mana_generator.png");
    private static final ResourceLocation FLAME =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/mana_generator/flame");


    public ManaGeneratorScreen(ManaGeneratorMenu menu, Inventory playerInventory,
            Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(GUI_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (menu.isLit()) {
            int burnTimeHeight = Mth.ceil((this.menu).getBurnProgress() * 13.0F) + 1;
            guiGraphics.blitSprite(FLAME, 14, 14, 0, 14 - burnTimeHeight, this.leftPos + 80,
                                   this.topPos + 36 + 14 - burnTimeHeight, 14, burnTimeHeight);
        }
        if (menu.getCookProgress() > 0) {
            int cookTimeHeight = (int) (this.menu.getCookProgress() * 52f);
            guiGraphics.fill(this.leftPos + 11, this.topPos + 17 + (52 - cookTimeHeight), this.leftPos + 11 + 10,
                             this.topPos + 17 + 52, 0xFF8D6ACC);
        }
    }
}
