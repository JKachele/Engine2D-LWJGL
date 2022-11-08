/******************************************
 *Project-------Engine2D-LWJGL
 *File----------JMath.java
 *Author--------Justin Kachele
 *Date----------10/10/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.util;

import org.joml.Vector2f;

/**
 * Contains all the game engine related math functions
 */
@SuppressWarnings("ALL")
public enum GameMath {;
    public static final float EPSILON = 0.00000001f;

    /**
     * Rotates a vector around a base point
     * @param vector Point to be rotated
     * @param base Point to rotate around
     * @param angle Angle to rotate in degrees
     */
    public static void rotate(Vector2f vector, Vector2f base, float angle) {
        // Move vector to origin
        float x = vector.x - base.x;
        float y = vector.y - base.y;

        float cos = (float) Math.cos(Math.toRadians(angle));
        float sin = (float) Math.sin(Math.toRadians(angle));

        // rotate vector around origin by angle degrees
        float xPrime = (x * cos) - (y * sin);
        float yPrime = (x * sin) + (y * cos);

        // Move resultant vector to original base point
        xPrime += base.x;
        yPrime += base.y;

        // Modify the original vector with the new point
        vector.x = xPrime;
        vector.y = yPrime;
    }

    /**
     * Tests equality between two floats with the given epsilon
     * @param a A floating point value
     * @param b A floating point value
     * @param epsilon The epsilon to test for equality
     * @return Boolean equality
     */
    public static boolean floatEquality(float a, float b, float epsilon) {
        return Math.abs(a - b) < (epsilon * Math.max(1.0f, Math.max(Math.abs(a), Math.abs(b))));
    }

    /**
     * Tests equality between two floats with a default epsilon of 0.00000001f
     * @param a A floating point value
     * @param b A floating point value
     * @return Boolean equality
     */
    public static boolean floatEquality(float a, float b) {
        return Math.abs(a - b) < (EPSILON * Math.max(1.0f, Math.max(Math.abs(a), Math.abs(b))));
    }

    /**
     * Tests equality between two Vectors with the given epsilon.
     * Both vector parameters must be equal for the vector to be equal
     * @param a A Vector2f value
     * @param b A Vector2f value
     * @param epsilon The epsilon to test for equality
     * @return Boolean equality
     */
    public static boolean vector2fEquality(Vector2f a, Vector2f b, float epsilon) {
        return floatEquality(a.x, b.x, epsilon) && floatEquality(a.y, b.y, epsilon);
    }

    /**
     * Tests equality between two Vectors with a default epsilon of 0.00000001f.
     * Both vector parameters must be equal for the vector to be equal
     * @param a A Vector2f value
     * @param b A Vector2f value
     * @return Boolean equality
     */
    public static boolean vector2fEquality(Vector2f a, Vector2f b) {
        return floatEquality(a.x, b.x) && floatEquality(a.y, b.y);
    }
}