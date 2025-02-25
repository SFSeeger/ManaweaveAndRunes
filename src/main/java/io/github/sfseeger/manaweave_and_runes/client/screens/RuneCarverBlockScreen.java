package io.github.sfseeger.manaweave_and_runes.client.screens;

import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.menus.RuneCarverMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static io.github.sfseeger.manaweave_and_runes.common.menus.RuneCarverMenu.CHISEL_SLOT;
import static io.github.sfseeger.manaweave_and_runes.common.menus.RuneCarverMenu.TEMPLATE_SLOT;

@OnlyIn(Dist.CLIENT)
public class RuneCarverBlockScreen extends ItemCombinerScreen<RuneCarverMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                  "textures/gui/container/rune_carver.png");

    private static final ResourceLocation CHISEL_SLOT_HINT =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/chisel_slot_hint");
    private static final ResourceLocation RUNE_SLOT_HINT =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/rune_slot_hint");
    private static final ResourceLocation TEMPLATE_SLOT_HINT =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/template_slot_hint");

    private static final ResourceLocation ERROR_SPRITE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "container/rune_carver/error_sprite");

    public RuneCarverBlockScreen(RuneCarverMenu menu, Inventory playerInventory,
                                 Component title) {
        super(menu, playerInventory, title, GUI_TEXTURE);
        this.titleLabelX = 44;
        this.titleLabelY = 15;
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
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, pPartialTick, mouseX, mouseY);

        if (!this.menu.getSlot(CHISEL_SLOT).hasItem()) {
            guiGraphics.blitSprite(CHISEL_SLOT_HINT, leftPos + 33, topPos + 48, 16, 16);
        }
        if (!this.menu.getSlot(TEMPLATE_SLOT).hasItem()) {
            guiGraphics.blitSprite(TEMPLATE_SLOT_HINT, leftPos + 51, topPos + 48, 16, 16);
        }

        if (this.hasRecipeError()) {
            this.renderErrorIcon(guiGraphics, this.leftPos, this.topPos);
        }
    }

    protected void renderErrorIcon(GuiGraphics guiGraphics, int x, int y) {
        if (this.hasRecipeError()) {
            guiGraphics.blitSprite(ERROR_SPRITE, x + 90, y + 46, 28, 21);
        }
    }

    private boolean hasRecipeError() {
        return (this.menu).getSlot(0).hasItem()
                && this.menu.getSlot(1).hasItem()
                && this.menu.getSlot(2).hasItem()
                && !this.menu.getSlot(this.menu.getResultSlot()).hasItem();
    }
}
