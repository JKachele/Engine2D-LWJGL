/******************************************
 *Project-------Learn-LWJGL
 *File----------IntersectionDetector2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.rigidbody;

import com.jkachele.game.physics2d.primitives.*;
import com.jkachele.game.util.GameMath;
import org.joml.Math;
import org.joml.Vector2f;

@SuppressWarnings("DuplicatedCode")
public class IntersectionDetector2D {
    // =========================================================
    // Point Vs. Primitive Tests
    // =========================================================
    public static boolean pointOnLine(Vector2f point, Line2D line) {
        if (GameMath.vector2fEquality(point, line.getStart()) ||
                GameMath.vector2fEquality(point, line.getEnd())) {
            return true;
        }

        // Form the equation y = mx + b equation for the line
        float dy = line.getEnd().y - line.getStart().y;
        float dx = line.getEnd().x - line.getStart().x;
        if (GameMath.floatEquality(dx, 0)) {
            return GameMath.floatEquality(point.x, line.getStart().x);
        }
        float m = dy / dx;

        float b = line.getEnd().y - (m * line.getEnd().x);

        // Check the point to line equation using floating point equality
        if (GameMath.floatEquality(point.y, m * point.x + b)) {
            // Check if the point is between the endpoints of the line
            Vector2f startToPoint = new Vector2f(point).sub(line.getStart());
            Vector2f startToEnd = new Vector2f(line.getEnd()).sub(line.getStart());

            float startToPointDot = startToEnd.dot(startToPoint);
            float startToEndDot = startToEnd.dot(startToEnd);

            // If the first dot product is > 0, and < second dot product the point is between the endpoints
            return ((startToPointDot > 0) && (startToPointDot < startToEndDot));
        }

        return false;
    }

    public static boolean pointInCircle(Vector2f point, Circle circle) {
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(circleCenter);

        return centerToPoint.lengthSquared() <= circle.getRadiusSquared();
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
        GameMath.rotate(localPoint, box2D.getCenter(),
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
        Vector2f center = box.getCenter();
        Vector2f localStart = new Vector2f(line.getStart());
        Vector2f localEnd = new Vector2f(line.getEnd());
        GameMath.rotate(localStart, center, theta);
        GameMath.rotate(localEnd, center, theta);

        Line2D localLine = new Line2D(localStart, localEnd);
        AABB2D aabb = new AABB2D(box.getMin(), box.getMax());

        return lineVsAABB2D(localLine, aabb);
    }

    // =========================================================
    // Circle Vs. Primitive Tests
    // =========================================================
    /**
     * Tests if a circle intersects a line
     * @param circle Circle Object
     * @param line Line2D Object
     * @return Boolean if the line intersects with the circle
     */
    public static boolean circleVsLine(Circle circle, Line2D line) {
        return lineVsCircle(line, circle);
    }

    /**
     * Tests if 2 circles are intersecting
     * @param circle1 Circle Object
     * @param circle2 Circle Object
     * @return Boolean if the 2 circles intersect
     */
    public static boolean circleVsCircle(Circle circle1, Circle circle2) {
        Vector2f vectorBetweenCenters = new Vector2f(circle1.getCenter()).sub(circle2.getCenter());
        float radiusSum = circle1.getRadius() + circle2.getRadius();

        return vectorBetweenCenters.lengthSquared() <= radiusSum * radiusSum;
    }

    /**
     * Tests if a circle is intersecting with an AABB
     * @param circle Circle Object
     * @param box AABB2D Object
     * @return Boolean if the circle intersects with the box
     */
    public static boolean CircleVsAABB2D(Circle circle, AABB2D box) {
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        Vector2f closestPointToCircle = new Vector2f(circle.getCenter());

        if (closestPointToCircle.x < min.x) {
            closestPointToCircle.x = min.x;
        } else if (closestPointToCircle.x > max.x) {
            closestPointToCircle.x = max.x;
        }

        if (closestPointToCircle.y < min.y) {
            closestPointToCircle.y = min.y;
        } else if (closestPointToCircle.y > max.y) {
            closestPointToCircle.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(circle.getCenter()).sub(closestPointToCircle);

        return circleToBox.lengthSquared() <= circle.getRadiusSquared();
    }

    /**
     *  Tests if a circle is intersecting with a Box2D
     * @param circle Circle Object
     * @param box Box2D Object
     * @return Boolean if the circle intersects with the box
     */
    public static boolean CircleVsBox2D(Circle circle, Box2D box) {
        // Treat the box like an AABB after rotating the components
        Vector2f min = new Vector2f();
        Vector2f max = new Vector2f(box.getHalfSize()).mul(2.0f);

        // Create a circle in the box's local space
        Vector2f r = new Vector2f(circle.getCenter()).sub(box.getCenter());
        GameMath.rotate(r, new Vector2f(0, 0), -box.getRigidBody().getRotationDeg());
        Vector2f localCirclePos = new Vector2f(r).add(box.getHalfSize());

        Vector2f closestPointToCircle = new Vector2f(localCirclePos);

        if (closestPointToCircle.x < min.x) {
            closestPointToCircle.x = min.x;
        } else if (closestPointToCircle.x > max.x) {
            closestPointToCircle.x = max.x;
        }

        if (closestPointToCircle.y < min.y) {
            closestPointToCircle.y = min.y;
        } else if (closestPointToCircle.y > max.y) {
            closestPointToCircle.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(localCirclePos).sub(closestPointToCircle);

        return circleToBox.lengthSquared() <= circle.getRadiusSquared();
    }

    // =========================================================
    // Raycasting
    // =========================================================
    /**
     * Tests if the raycast intersects with a circle
     * @param circle Circle to test the raycast against
     * @param ray The ray to be cast
     * @param result RaycastResult object to hold results of the raycast. Null if not needed
     * @return Boolean of the raycast intersected with the circle
     */
    public static boolean raycast(Circle circle, Ray2D ray, RaycastResult result) {
        RaycastResult.reset(result);

        Vector2f originToCircle = new Vector2f(circle.getCenter()).sub(ray.getOrigin());
        float radiusSquared = circle.getRadiusSquared();
        float originToCircleSquared = originToCircle.lengthSquared();

        // Project the vector from the ray origin onto the direction of the ray
        float a = originToCircle.dot(ray.getDirection());
        float bSq = originToCircleSquared - (a * a);
        if (radiusSquared - bSq < 0.0f) {
            return false;
        }

        float f = Math.sqrt(radiusSquared - bSq);   // Why rayCasts are slow
        float t;

        if (originToCircleSquared < radiusSquared) {
            // Ray starts inside circle
            t = a + f;
        } else {
            t = a - f;
        }

        if (result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()
                    .add(new Vector2f(ray.getDirection()).mul(t)));
            Vector2f normal = new Vector2f(point).sub(circle.getCenter());
            normal.normalize();
            result.init(point, normal, t, true);
        }

        return true;
    }

    /**
     * Tests if the raycast intersects with an AABB
     * @param box AABB2D to test the raycast against
     * @param ray The ray to be cast
     * @param result RaycastResult object to hold results of the raycast. Null if not needed
     * @return Boolean of the raycast intersected with the AABB
     */
    public static boolean raycast(AABB2D box, Ray2D ray, RaycastResult result) {
        RaycastResult.reset(result);

        Vector2f unitVector = ray.getDirection();
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0.0f;
        unitVector.y = (unitVector.y!= 0)? 1.0f / unitVector.y :0.0f;

        Vector2f min = box.getMin();
        min.sub(ray.getOrigin()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(ray.getOrigin()).mul(unitVector);

        float tMin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tMax = Math.min(Math.max(min.x, max.x), Math.min(min.y, max.y));

        if (tMax < 0.0f || tMin > tMax) {
            return false;
        }

        float t = (tMin < 0f) ? tMax : tMin;
        boolean hit = t > 0f;
        if (!hit) {
            return false;
        }

        if (result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()
                    .add(new Vector2f(ray.getDirection()).mul(t)));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();

            result.init(point, normal, t, true);
        }

        return true;
    }

    /**
     * Tests if the raycast intersects with a Box2D
     * @param box Box2D to test the raycast against
     * @param ray The ray to be cast
     * @param result RaycastResult object to hold results of the raycast. Null if not needed
     * @return Boolean of the raycast intersected with the Box2D
     */
    public static boolean raycast(Box2D box, Ray2D ray, RaycastResult result) {
        RaycastResult.reset(result);

        Vector2f xAxis = new Vector2f(1, 0);
        Vector2f yAxis = new Vector2f(0, 1);
        Vector2f halfSize = box.getHalfSize();
        GameMath.rotate(xAxis, new Vector2f(0, 0), -box.getRigidBody().getRotationDeg());
        GameMath.rotate(yAxis, new Vector2f(0, 0), -box.getRigidBody().getRotationDeg());

        Vector2f p = new Vector2f(box.getCenter()).sub(ray.getOrigin());

        // Project the direction of the ray onto each axis of the box
        Vector2f f = new Vector2f(xAxis.dot(ray.getDirection()), yAxis.dot(ray.getDirection()));

        // Project p onto each axis of the box
        Vector2f e = new Vector2f(xAxis.dot(p), yAxis.dot(p));

        float[] tArray = {0, 0, 0, 0};

        for (int i = 0; i < 2; i++) {
            if (GameMath.floatEquality(f.get(i), 0)) {
                // If the ray is parallel to the current axis, and the ray isn't inside the box, there is no hit
                if (-e.get(i) - halfSize.get(i) > 0 || -e.get(i) - halfSize.get(i) < 0) {
                    return false;
                }
                // Set f to small size to avoid divide by zero errors
                f.setComponent(i, 0.00001f);
            }
            tArray[i * 2] = (e.get(i) + halfSize.get(i)) / f.get(i);     // tMax for the axis
            tArray[i * 2 + 1] = (e.get(i) - halfSize.get(i)) / f.get(i); // tMin for the axis
        }

        float tMin = Math.max(Math.min(tArray[0], tArray[1]), Math.min(tArray[2], tArray[3]));
        float tMax = Math.min(Math.max(tArray[0], tArray[1]), Math.max(tArray[2], tArray[3]));

        float t = (tMin < 0f) ? tMax : tMin;
        boolean hit = t > 0f;
        if (!hit) {
            return false;
        }

        if (result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()
                    .add(new Vector2f(ray.getDirection()).mul(t)));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();

            result.init(point, normal, t, true);
        }

        return true;
    }
}