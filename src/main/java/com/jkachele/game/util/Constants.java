/******************************************
 *Project-------Learn-LWJGL
 *File----------Settings.java
 *Author--------Justin Kachele
 *Date----------10/9/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.util;

public final class Constants {
    public static final int GRID_WIDTH = 32;
    public static final int GRID_HEIGHT = 32;
    public static final float EPSILON = 0.00000001f;

    public static boolean floatEquality(float x, float y, float epsilon) {
        return Math.abs(x - y) < epsilon;
    }

    public static boolean floatEquality(float x, float y) {
        return Math.abs(x - y) < EPSILON;
    }
}
