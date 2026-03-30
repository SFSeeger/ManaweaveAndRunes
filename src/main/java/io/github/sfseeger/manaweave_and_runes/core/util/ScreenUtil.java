package io.github.sfseeger.manaweave_and_runes.core.util;

public class ScreenUtil {
    public static boolean isMouseInBounds(int x, int y, int width, int height, int mouseX, int mouseY) {
        return isMouseInBounds(x, y, width, height, (double) mouseX, (double) mouseY);
    }

    public static boolean isMouseInBounds(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
