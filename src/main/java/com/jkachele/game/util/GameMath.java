/******************************************
 *Project-------Learn-LWJGL
 *File----------JMath.java
 *Author--------Justin Kachele
 *Date----------10/10/2022
 *License-------GNU GENERAL PUBLIC LICENSE
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
     * Rotates a vector around a base point angle degrees
     */
    public static void rotate(Vector2f vector, Vector2f base, float angleDegrees) {
        // Move vector to origin
        float x = vector.x - base.x;
        float y = vector.y - base.y;

        float cos = (float) Math.cos(Math.toRadians(angleDegrees));
        float sin = (float) Math.sin(Math.toRadians(angleDegrees));

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

    public static boolean floatEquality(float a, float b, float epsilon) {
        return Math.abs(a - b) < (epsilon * Math.max(1.0f, Math.max(Math.abs(a), Math.abs(b))));
    }

    /**
     * Compares the the difference between the 2 floats with an addaptive epsilon of 0.00000001f
     */
    public static boolean floatEquality(float a, float b) {
        return Math.abs(a - b) < (EPSILON * Math.max(1.0f, Math.max(Math.abs(a), Math.abs(b))));
    }

    public static boolean vector2fEquality(Vector2f a, Vector2f b, float epsilon) {
        return floatEquality(a.x, b.x, epsilon) && floatEquality(a.y, b.y, epsilon);
    }

    /**
     * Compares the the difference between the 2 Vector's components with an addaptive epsilon of 0.00000001f
     */
    public static boolean vector2fEquality(Vector2f a, Vector2f b) {
        return floatEquality(a.x, b.x) && floatEquality(a.y, b.y);
    }
}