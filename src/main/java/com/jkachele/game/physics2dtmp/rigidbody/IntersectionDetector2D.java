/******************************************
 *Project-------Engine2D-LWJGL
 *File----------IntersectionDetector2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.physics2dtmp.rigidbody;

import com.jkachele.game.physics2dtmp.primitives.*;
import com.jkachele.game.util.GameMath;
import org.joml.Math;
import org.joml.Vector2f;

@SuppressWarnings("DuplicatedCode")
public class IntersectionDetector2D {
    // =========================================================
    // Point Vs. Primitive Tests
    // =========================================================
    /**
     * Tests if a point intersects with a Line2D Object.
     * @param point Vector2F Object
     * @param line Line2D Object
     * @return Boolean if the point is on the line
     */
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

    /**
     * Tests if a Point intersects with a Circle Object.
     * @param point Vector2f object
     * @param circle Circle object
     * @return Boolean if the point is inside the circle
     */
    public static boolean pointInCircle(Vector2f point, Circle circle) {
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(circleCenter);

        return centerToPoint.lengthSquared() <= circle.getRadiusSquared();
    }

    /**
     * Tests if a Point intersects with an AABB Object.
     * @param point Vector2f object
     * @param box AABB2D object
     * @return Boolean if the point is inside the box
     */
    public static boolean pointInAABB2D(Vector2f point, AABB2D box) {
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return (point.x >= min.x) && (point.x <= max.x) &&
                (point.y >= min.y) && (point.y <= max.y);
    }

    /**
     * Tests if a Point intersects with a Box2D Object.
     * @param point Vector2f object
     * @param box Box2D object
     * @return Boolean if the point is inside the box
     */
    public static boolean pointInBox2D(Vector2f point, Box2D box) {
        // Translate the point into the box's local coordinate space
        Vector2f localPoint = new Vector2f(point);
        GameMath.rotate(localPoint, box.getCenter(),
                box.getRigidbody().getRotationDeg());

        Vector2f min = box.getLocalMin();
        Vector2f max = box.getLocalMax();

        return (localPoint.x >= min.x) && (localPoint.x <= max.x) &&
                (localPoint.y >= min.y) && (localPoint.y <= max.y);
    }

    // =========================================================
    // Line Vs. Primitive Tests
    // =========================================================
    /**
     * Tests if a Line2D object intersects with a Circle Object.
     * Go here: <a href="https://youtu.be/Yx1fo2YLJOs">...</a> for explanation
     * @param line Line2D Object
     * @param circle Circle Object
     * @return Boolean if the line intersects with the box
     */
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
     * Tests if a Line2D object intersects with an AABB2D Object.
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
     * Tests if a Line2D object intersects with a Box2D Object.
     * Go here: <a href="https://youtu.be/eo_hrg6kVA8">...</a> for explanation
     * @param line Line2D Object
     * @param box Box2D Object
     * @return Boolean if the line intersects with the box
     */
    public static boolean lineVsBox2D(Line2D line, Box2D box) {
        float theta = box.getRigidbody().getRotationDeg();
        Vector2f center = box.getCenter();
        Vector2f localStart = new Vector2f(line.getStart());
        Vector2f localEnd = new Vector2f(line.getEnd());
        GameMath.rotate(localStart, center, theta);
        GameMath.rotate(localEnd, center, theta);

        Line2D localLine = new Line2D(localStart, localEnd);
        AABB2D aabb = new AABB2D(box.getLocalMin(), box.getLocalMax());

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
        GameMath.rotate(r, new Vector2f(0, 0), -box.getRigidbody().getRotationDeg());
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
        GameMath.rotate(xAxis, new Vector2f(0, 0), -box.getRigidbody().getRotationDeg());
        GameMath.rotate(yAxis, new Vector2f(0, 0), -box.getRigidbody().getRotationDeg());

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

    // =========================================================
    // AABB2D Vs. Primitive Tests
    // =========================================================
    /**
     * Tests if a circle is intersecting with an AABB
     * @param circle Circle Object
     * @param box AABB2D Object
     * @return Boolean if the circle intersects with the box
     */
    public static boolean aabb2DVsCircle(AABB2D box, Circle circle) {
        return CircleVsAABB2D(circle, box);
    }
    /**
     * Tests if an AABB2D object intersects with another AABB2D Object using the Separating Axis Theorem.
     * Go here: <a href="https://youtu.be/Nm1Cgmbg5SQ">...</a> for explanation
     * @param box1 AABB2D Object
     * @param box2 AABB2D Object
     * @return Boolean if the AABBs intersect
     */
    public static boolean aabb2DVsAABB2D(AABB2D box1, AABB2D box2) {
        // AABB aligned on (1, 0) and (0, 1) axes
        Vector2f[] axesToTest = {new Vector2f(1, 0), new Vector2f(0, 1)};
        for (Vector2f axisToTest : axesToTest) {
            if (!overlapOnAxis(box1, box2, axisToTest)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Tests if an AABB2D object intersects with a Box2D Object using the Separating Axis Theorem.
     * Go here: <a href="https://youtu.be/Nm1Cgmbg5SQ">...</a> for explanation
     * @param aabb AABB2D Object
     * @param box Box2D Object
     * @return Boolean if the AABB intersects with the box
     */
    public static boolean aabb2DVsBox2D(AABB2D aabb, Box2D box) {
        Vector2f[] axesToTest = {
                new Vector2f(1, 0), new Vector2f(0, 1),
                new Vector2f(1, 0), new Vector2f(0, 1)
        };
        GameMath.rotate(axesToTest[2], new Vector2f(0, 0), box.getRigidbody().getRotationDeg());
        GameMath.rotate(axesToTest[3], new Vector2f(0, 0), box.getRigidbody().getRotationDeg());

        for (Vector2f axisToTest : axesToTest) {
            if (!overlapOnAxis(aabb, box, axisToTest)) {
                return false;
            }
        }

        return true;
    }

    // =========================================================
    // Separating Axis Theorem Helper Methods
    // =========================================================
    private static boolean overlapOnAxis(AABB2D box1, AABB2D box2, Vector2f axis) {
        axis.normalize();

        Vector2f interval1 = getInterval(box1, axis);
        Vector2f interval2 = getInterval(box2, axis);

        return ((interval2.x <= interval1.y) && (interval1.x <= interval2.y));
    }

    private static boolean overlapOnAxis(AABB2D box1, Box2D box2, Vector2f axis) {
        axis.normalize();

        Vector2f interval1 = getInterval(box1, axis);
        Vector2f interval2 = getInterval(box2, axis);

        return ((interval2.x <= interval1.y) && (interval1.x <= interval2.y));
    }

    private static boolean overlapOnAxis(Box2D box1, Box2D box2, Vector2f axis) {
        axis.normalize();

        Vector2f interval1 = getInterval(box1, axis);
        Vector2f interval2 = getInterval(box2, axis);

        return ((interval2.x <= interval1.y) && (interval1.x <= interval2.y));
    }

    private static Vector2f getInterval(AABB2D rect, Vector2f axis) {
        Vector2f result = new Vector2f(0, 0);

        Vector2f min = rect.getMin();
        Vector2f max = rect.getMax();

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),     // Bottom left
                new Vector2f(min.x, max.y),     // Top left
                new Vector2f(max.x, max.y),     // Top right
                new Vector2f(max.x, min.y),     // Bottom right
        };

        result.x = axis.dot(vertices[0]);
        result.y = axis.dot(vertices[0]);
        for (int i = 1; i < vertices.length; i++) {
            float projection = axis.dot(vertices[i]);
            if (projection < result.x) {
                result.x = projection;
            }
            if (projection > result.y) {
                result.y = projection;
            }
        }

        return result;
    }

    private static Vector2f getInterval(Box2D rect, Vector2f axis) {
        Vector2f result = new Vector2f(0, 0);

        Vector2f min = rect.getLocalMin();
        Vector2f max = rect.getLocalMax();

        Vector2f[] vertices = rect.getVertices();

        result.x = axis.dot(vertices[0]);
        result.y = axis.dot(vertices[0]);
        for (int i = 1; i < vertices.length; i++) {
            float projection = axis.dot(vertices[i]);
            if (projection < result.x) {
                result.x = projection;
            }
            if (projection > result.y) {
                result.y = projection;
            }
        }

        return result;
    }
}