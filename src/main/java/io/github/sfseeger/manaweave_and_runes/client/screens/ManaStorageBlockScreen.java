package io.github.sfseeger.manaweave_and_runes.client.screens;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import io.github.sfseeger.manaweave_and_runes.common.blockentities.ManaStorageBlockEntity;
import io.github.sfseeger.manaweave_and_runes.common.menus.ManaStorageBlockMenu;
import io.github.sfseeger.manaweave_and_runes.core.util.ScreenUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaStorageBlockScreen extends AbstractContainerScreen<ManaStorageBlockMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID,
                                                  "textures/gui/container/mana_storage.png");
    private static final ResourceLocation FALLBACK_ICON =
            ResourceLocation.fromNamespaceAndPath(ManaweaveAndRunes.MODID, "textures/gui/sprites/container/fire_mana");


    private int uiX;
    private int uiY;


    public ManaStorageBlockScreen(ManaStorageBlockMenu menu, Inventory playerInventory,
            Component title) {
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
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        for (int i = 0; i < ManaStorageBlockEntity.MANA_SLOTS; i++) {
            if (ScreenUtil.isMouseInBounds(uiX + 44 + i * 18, uiY + 56, 16, 16, x, y)) {
                Mana mana = menu.getManaInSlot(i);
                if (mana != null)
                    guiGraphics.renderTooltip(this.font, mana.getName().withColor(mana.properties().getColor()), x, y);
            } else if (ScreenUtil.isMouseInBounds(uiX + 50 + i * 18, uiY + 15, 3, 36, x, y)) {
                Mana mana = menu.getManaInSlot(i);
                if (mana != null)
                    guiGraphics.renderTooltip(this.font, Component.translatable("lore.manaweave_and_runes.mana_stored",
                                                                                String.format("%,d",
                                                                                              menu.getManaStored(mana)),
                                                                                String.format("%,d",
                                                                                              menu.getManaCapacity()))
                            .withColor(mana.properties().getColor()), x, y);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(GUI_TEXTURE, uiX, uiY, 0, 0, this.imageWidth, this.imageHeight);

        for (int i = 0; i < ManaStorageBlockEntity.MANA_SLOTS; i++) {
            Mana mana = menu.getManaInSlot(i);
            if (mana != null) {
                float fill = menu.getManaPercentage(mana);
                guiGraphics.fill(uiX + 50 + i * 18, uiY + 51 - (int) (36 * fill), uiX + 53 + i * 18, uiY + 51,
                                 0xFF000000 + mana.properties().getColor());
                guiGraphics.blitSprite(mana.properties().getIcon().orElse(FALLBACK_ICON), uiX + 44 + i * 18, uiY + 56,
                                       16, 16);
            }
        }
    }

}
