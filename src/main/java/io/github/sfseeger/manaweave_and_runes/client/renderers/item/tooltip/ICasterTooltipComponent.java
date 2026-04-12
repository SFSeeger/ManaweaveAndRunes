package io.github.sfseeger.manaweave_and_runes.client.renderers.item.tooltip;

import io.github.sfseeger.lib.common.spells.Spell;
import io.github.sfseeger.manaweave_and_runes.ManaweaveAndRunes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ICasterTooltipComponent extends ManaDisplayTooltipComponent {
    final @Nullable Spell currentSpell;
    final ResourceLocation COOLDOWN_ICON = ManaweaveAndRunes.asResource("textures/gui/book/clock");

    public ICasterTooltipComponent(@Nullable Spell currentSpell) {
        super(currentSpell == null ? Map.of() : currentSpell.getManaCost());
        this.currentSpell = currentSpell;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if (currentSpell != null) {
            guiGraphics.pose().pushPose();
            {
                guiGraphics.drawString(font,
                                       Component.translatable("tooltip.manaweave_and_runes.caster",
                                                              currentSpell.getName()),
                                       x + ICON_PADDING, y, 0xFFFFFF);
//                guiGraphics.blit(COOLDOWN_ICON, x, y + 5, 0, 0, 16, 16);
                guiGraphics.drawString(font, Component.literal(currentSpell.getCooldown() / 20.0 + "s"),
                                       x + ICON_PADDING,y + 10, 0xFFFFFF);

            }
            guiGraphics.pose().popPose();
            super.renderImage(font, x, y + 20, guiGraphics);
        }
    }

    @Override
    public int getHeight() {
        return 20 + super.getHeight();
    }
}
