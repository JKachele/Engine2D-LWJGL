/******************************************
 *Project-------Learn-LWJGL
 *File----------IntersectionDetector2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.rigidbody;

import com.jkachele.game.renderer.Line2D;
import com.jkachele.game.util.Constants;
import org.joml.Vector2f;

public class IntersectionDetector2D {
    // =========================================================
    // Point Vs. Primitive Tests
    // =========================================================
    public static boolean pointOnLine(Vector2f point, Line2D line) {
        // Form the equation y = mx + b equation for the line
        float dy = line.getEnd().y - line.getStart().y;
        float dx = line.getEnd().x - line.getStart().x;
        float m = dy / dx;

        float b = line.getEnd().y - (m * line.getEnd().x);

        // Check the point to line equation using floating point equality
        return (Math.abs(point.y - m * point.x + b) < Constants.EPSILON);
    }

    // =========================================================
    // Line Vs. Primitive Tests
    // =========================================================

}
