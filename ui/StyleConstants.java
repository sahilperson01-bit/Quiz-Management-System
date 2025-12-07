package com.quiz.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StyleConstants {
    public enum Theme {
        LIGHT, DARK
    }

    private static Theme currentTheme = Theme.LIGHT;

    // Light Theme Colors
    private static final Color PRIMARY_COLOR_LIGHT = new Color(41, 128, 185); // Deep Blue
    private static final Color SECONDARY_COLOR_LIGHT = new Color(236, 240, 241); // Light Gray
    private static final Color ACCENT_COLOR_LIGHT = new Color(46, 204, 113); // Green
    private static final Color TEXT_COLOR_LIGHT = new Color(44, 62, 80); // Dark Blue/Gray
    private static final Color WHITE_LIGHT = Color.WHITE;

    // Dark Theme Colors
    private static final Color PRIMARY_COLOR_DARK = new Color(52, 152, 219); // Brighter Blue
    private static final Color SECONDARY_COLOR_DARK = new Color(44, 62, 80); // Dark Gray/Blue
    private static final Color ACCENT_COLOR_DARK = new Color(39, 174, 96); // Darker Green
    private static final Color TEXT_COLOR_DARK = new Color(236, 240, 241); // Light Gray
    private static final Color WHITE_DARK = new Color(52, 73, 94); // Darker Gray

    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }

    public static Theme getTheme() {
        return currentTheme;
    }

    public static void toggleTheme() {
        currentTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
    }

    public static Color getPrimaryColor() {
        return currentTheme == Theme.LIGHT ? PRIMARY_COLOR_LIGHT : PRIMARY_COLOR_DARK;
    }

    public static Color getSecondaryColor() {
        return currentTheme == Theme.LIGHT ? SECONDARY_COLOR_LIGHT : SECONDARY_COLOR_DARK;
    }

    public static Color getAccentColor() {
        return currentTheme == Theme.LIGHT ? ACCENT_COLOR_LIGHT : ACCENT_COLOR_DARK;
    }

    public static Color getTextColor() {
        return currentTheme == Theme.LIGHT ? TEXT_COLOR_LIGHT : TEXT_COLOR_DARK;
    }

    public static Color getCardColor() {
        return currentTheme == Theme.LIGHT ? WHITE_LIGHT : WHITE_DARK;
    }

    public static JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!getModel().isPressed()) {
                    g.setColor(bgColor);
                } else {
                    g.setColor(bgColor.darker());
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btn.setFont(BUTTON_FONT);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false); // Important for custom painting
        btn.setOpaque(false);
        return btn;
    }

    public static JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(BODY_FONT);
        // We might need to update this dynamically, but for now let's keep it simple
        // Ideally components should listen to theme changes or be recreated
        return field;
    }
    
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        label.setForeground(getPrimaryColor());
        return label;
    }
}
