/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Color.java
 *Author--------Justin Kachele
 *Date----------9/24/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.util;

import org.joml.Vector4f;

public final class Color {

    public static final Color WHITE         = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color LIGHT_GRAY    = new Color(0.75f, 0.75f, 0.75f, 1.0f);
    public static final Color GRAY          = new Color(0.5f, 0.5f, 0.5f, 1.0f);
    public static final Color DARK_GRAY     = new Color(0.25f, 0.25f, 0.25f, 1.0f);
    public static final Color BLACK         = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color RED           = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN         = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE          = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    public static final Color YELLOW        = new Color(1.0f, 1.0f, 0.0f, 1.0f);
    public static final Color CYAN          = new Color(0.0f, 1.0f, 1.0f, 1.0f);
    public static final Color MAGENTA       = new Color(1.0f, 0.0f, 1.0f, 1.0f);
    public static final Color ORANGE        = new Color(1.0f, 0.8f, 0.0f, 1.0f);
    public static final Color PINK          = new Color(1.0f, 0.7f, 0.7f, 1.0f);
    public static final Color NULL          = new Color(0.0f, 0.0f, 0.0f, 0.0f);

    private float red;
    private float green;
    private float blue;
    private float alpha;

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1.0f;
    }

    public Color darker() {
        return new Color(red * 0.75f, green * 0.75f, blue * 0.75f, alpha);
    }

    public Color scale(float d) {
        return new Color(Math.min(red * d, 1.0f), Math.min(green * d, 1.0f), Math.min(blue * d, 1.0f), alpha);
    }

    public Color lighter() {
        return new Color(Math.min(red * 1.25f, 1.0f), Math.min(green * 1.25f, 1.0f), Math.min(blue * 1.25f, 1.0f), alpha);
    }

    public Color fadeToColor(Color target) {
        float dRed      = (target.getRed() - red) / 100;
        float dGreen    = (target.getGreen() - green) / 100;
        float dBlue     = (target.getBlue() - blue) / 100;

        return new Color(red + dRed, green + dGreen, blue + dBlue, alpha);
    }

    public Color fadeToColor(Color target, float d) {
        float dRed      = (target.getRed() - red) / (100 * d);
        float dGreen    = (target.getGreen() - green) / (100 * d);
        float dBlue     = (target.getBlue() - blue) / (100 * d);

        return new Color(red + dRed, green + dGreen, blue + dBlue, alpha);
    }

    public float[] getComponents() {
        return new float[]{red, green, blue, alpha};
    }

    public Vector4f toVector() {
        return new Vector4f(red, green, blue, alpha);
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass()!= o.getClass()) return false;
        Color other = (Color) o;
        if (Float.floatToIntBits(red) != Float.floatToIntBits(other.red))
            return false;
        if (Float.floatToIntBits(green)!= Float.floatToIntBits(other.green))
            return false;
        if (Float.floatToIntBits(blue)!= Float.floatToIntBits(other.blue))
            return false;
        if (Float.floatToIntBits(alpha)!= Float.floatToIntBits(other.alpha))
            return false;
        return true;
    }
}
