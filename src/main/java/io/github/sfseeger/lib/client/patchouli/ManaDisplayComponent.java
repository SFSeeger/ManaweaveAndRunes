package io.github.sfseeger.lib.client.patchouli;

import io.github.sfseeger.lib.common.mana.Mana;
import io.github.sfseeger.lib.core.ManaweaveAndRunesRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.List;
import java.util.function.UnaryOperator;

public class ManaDisplayComponent implements ICustomComponent {
    private transient int x, y;
    private String mana;
    private String amount;

    private transient Mana manaType;
    private transient Number manaAmount;

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
    }

    @Override
    public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        if (manaType != null) {
            graphics.blitSprite(manaType.properties().getIcon().get(), x, y, 16, 16);
            graphics.drawString(font, manaAmount + "", x + 16, y + 4, -1, true);
            if (context.isAreaHovered(mouseX, mouseY, x, y, 16 + 16 + 5, 16)) {
                context.setHoverTooltipComponents(
                        List.of(manaType.getName().withColor(manaType.properties().getColor())));
            }
        }
    }


    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup, HolderLookup.Provider registries) {
        ResourceLocation manaTypeLocation =
                ResourceLocation.parse(lookup.apply(IVariable.wrap(mana, registries)).asString());
        manaType = ManaweaveAndRunesRegistries.MANA_REGISTRY.get(manaTypeLocation);
        manaAmount = lookup.apply(IVariable.wrap(amount, registries)).asNumber();
    }
}
