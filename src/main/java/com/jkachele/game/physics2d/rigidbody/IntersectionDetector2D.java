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
import org.joml.Math;
import org.joml.Vector2f;

public class IntersectionDetector2D {
    // =========================================================
    // Point Vs. Primitive Tests
    // =========================================================
    public static boolean pointOnLine(Vector2f point, Line2D line2D) {
        // Form the equation y = mx + b equation for the line
        float dy = line2D.getEnd().y - line2D.getStart().y;
        float dx = line2D.getEnd().x - line2D.getStart().x;
        if (GameMath.floatEquality(dx, 0)) {
            return GameMath.floatEquality(point.x, line2D.getStart().x);
        }
        float m = dy / dx;

        float b = line2D.getEnd().y - (m * line2D.getEnd().x);

        // TODO: CHECK IF POINT IS WITHIN THE LIMITS OF THE SEGMENT

        // Check the point to line equation using floating point equality
        return (GameMath.floatEquality(point.y, m * point.x + b));
    }

    public static boolean pointInCircle(Vector2f point, Circle circle) {
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(circleCenter);

        return centerToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean pointInAABB2D(Vector2f point, AABB2D aabb2D) {
        Vector2f min = aabb2D.getMin();
        Vector2f max = aabb2D.getMax();

        return (point.x >= min.x) && (point.x <= max.x) &&
                (point.y >= min.y) && (point.y <= max.y);
    }

    public static boolean pointInBox2D(Vector2f point, Box2D box2D) {
        // Translate the point into the box's local coordinate space
        Vector2f localPoint = new Vector2f(point);
        GameMath.rotate(localPoint, box2D.getRigidBody().getPosition(),
                box2D.getRigidBody().getRotationDeg());

        Vector2f min = box2D.getMin();
        Vector2f max = box2D.getMax();

        return (localPoint.x >= min.x) && (localPoint.x <= max.x) &&
                (localPoint.y >= min.y) && (localPoint.y <= max.y);
    }

    /**
     * Go here: <a href="https://youtu.be/Yx1fo2YLJOs">...</a> for explanation
     * @param line Line2D Object
     * @param circle Circle Object
     * @return Boolean if the line intersects with the box
     */
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

    /**
     * Go here: <a href="https://youtu.be/eo_hrg6kVA8">...</a> for explanation
     * @param line Line2D Object
     * @param box AABB2D Object
     * @return Boolean if the line intersects with the box
     */
    public static boolean lineVsAABB2D(Line2D line, AABB2D box) {
        if (pointInAABB2D(line.getStart(), box) || pointInAABB2D(line.getEnd(), box)) {
            return true;
        }
        Vector2f unitVector = new Vector2f(line.getEnd()).sub(line.getStart());
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0.0f;
        unitVector.y = (unitVector.y!= 0)? 1.0f / unitVector.y :0.0f;

        Vector2f min = box.getMin();
        min.sub(line.getStart()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(line.getStart()).mul(unitVector);

        float tMin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tMax = Math.min(Math.max(min.x, max.x), Math.min(min.y, max.y));

        if (tMax < 0.0f || tMin > tMax) {
            return false;
        }

        float t = (tMin < 0f) ? tMax : tMin;

        return (t > 0.0f) && (t * t < line.lengthSquared());
    }

    /**
     * Go here: <a href="https://youtu.be/eo_hrg6kVA8">...</a> for explanation
     * @param line Line2D Object
     * @param box Box2D Object
     * @return Boolean if the line intersects with the box
     */
    public static boolean lineVsBox2D(Line2D line, Box2D box) {
        float theta = box.getRigidBody().getRotationDeg();
        Vector2f center = box.getRigidBody().getPosition();
        Vector2f localStart = new Vector2f(line.getStart());
        Vector2f localEnd = new Vector2f(line.getEnd());
        GameMath.rotate(localStart, center, theta);
        GameMath.rotate(localEnd, center, theta);

        Line2D localLine = new Line2D(localStart, localEnd);
        AABB2D aabb = new AABB2D(box.getMin(), box.getMax());

        return lineVsAABB2D(localLine, aabb);
    }
}