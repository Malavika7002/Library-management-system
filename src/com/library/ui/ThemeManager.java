package com.library.ui;

import java.awt.Color;
import java.awt.Font;

public class ThemeManager {
    // Exact Color Palette Requested
    public static final Color COLOR_BUTTON = new Color(0x2F, 0x48, 0x6D);    // #2f486d - Primary Buttons / Accents
    public static final Color COLOR_NAVY_BG = new Color(0x02, 0x24, 0x35);   // #022435 - Header / Primary Blue
    public static final Color COLOR_SKIN_BG = new Color(0xF3, 0xEA, 0xE0);   // #F3EAE0 - Light Skin / Cream Background

    public static Color PANEL_BG = COLOR_SKIN_BG;
    public static Color CARD_BG = Color.WHITE;
    public static Color TEXT_PRIMARY = COLOR_NAVY_BG;
    public static Color TEXT_WHITE = Color.WHITE;

    public static Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 13);

    public static void setDarkMode(boolean enable) {
        if (enable) {
            PANEL_BG = COLOR_NAVY_BG;
            CARD_BG = new Color(0x0A, 0x32, 0x49);
            TEXT_PRIMARY = COLOR_SKIN_BG;
        } else {
            PANEL_BG = COLOR_SKIN_BG;
            CARD_BG = Color.WHITE;
            TEXT_PRIMARY = COLOR_NAVY_BG;
        }
    }
}