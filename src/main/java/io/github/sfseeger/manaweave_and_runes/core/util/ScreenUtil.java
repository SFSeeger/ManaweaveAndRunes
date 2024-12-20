package io.github.sfseeger.manaweave_and_runes.core.util;

public class ScreenUtil {
    public static boolean isMouseInBounds(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
