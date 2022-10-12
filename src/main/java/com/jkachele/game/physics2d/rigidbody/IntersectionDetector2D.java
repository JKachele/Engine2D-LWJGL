/******************************************
 *Project-------Learn-LWJGL
 *File----------IntersectionDetector2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.rigidbody;

import com.jkachele.game.physics2d.primitives.AABB2D;
import com.jkachele.game.physics2d.primitives.Box2D;
import com.jkachele.game.physics2d.primitives.Circle;
import com.jkachele.game.renderer.Line2D;
import com.jkachele.game.util.GameMath;
import org.joml.Vector2f;

public class IntersectionDetector2D {
    // =========================================================
    // Point Vs. Primitive Tests
    // =========================================================
    public static boolean pointOnLine(Vector2f point, Line2D line) {
        // Form the equation y = mx + b equation for the line
        float dy = line.getEnd().y - line.getStart().y;
        float dx = line.getEnd().x - line.getStart().x;
        if (GameMath.floatEquality(dx, 0)) {
            return GameMath.floatEquality(point.x, line.getStart().x);
        }
        float m = dy / dx;

        float b = line.getEnd().y - (m * line.getEnd().x);

        // Check the point to line equation using floating point equality
        return (GameMath.floatEquality(point.y, m * point.x + b));
    }

    public static boolean pointInCircle(Vector2f point, Circle circle) {
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(circleCenter);

        return centerToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean pointInAABB2D(Vector2f point, AABB2D box) {
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return (point.x >= min.x) && (point.x <= max.x) &&
                (point.y >= min.y) && (point.y <= max.y);
    }

    public static boolean pointInBox2D(Vector2f point, Box2D box) {
        // Translate the point into the box's local coordinate space
        Vector2f localPoint = new Vector2f(point);
        GameMath.rotate(localPoint, box.getRigidBody().getPosition(),
                box.getRigidBody().getRotationDeg());

        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return (localPoint.x >= min.x) && (localPoint.x <= max.x) &&
                (localPoint.y >= min.y) && (localPoint.y <= max.y);
    }

    // =========================================================
    // Line Vs. Primitive Tests
    // =========================================================
    public static boolean lineVsCircle(Line2D line, Circle circle) {
        if (pointInCircle(line.getStart(), circle) || pointInCircle(line.getEnd(), circle)) {
            return true;
        }
        Vector2f ab = new Vector2f(line.getEnd()).sub(line.getStart());

        // Project the Circle center point onto the line ab
        // parameterized position t
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToLineStart = new Vector2f(circleCenter).sub(line.getStart());
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        // Return false if the point isn't on the line
        if (t < 0.0f || t > 1.0f) {
            return false;
        }

        // Find the closest point to line segment
        Vector2f closestPoint = new Vector2f(line.getStart()).add(ab.mul(t));

        return pointInCircle(closestPoint, circle);
    }

    public static boolean lineVsAABB2D(Line2D line, AABB2D box) {
        return false;
    }

    public static  boolean lineVsBox2D(Line2D line, Box2D box) {
        return false;
    }
}
