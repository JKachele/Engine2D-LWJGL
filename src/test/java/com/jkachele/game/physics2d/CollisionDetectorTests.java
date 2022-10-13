/******************************************
 *Project-------Learn-LWJGL
 *File----------CollisionDetectorTests.java
 *Author--------Justin Kachele
 *Date----------10/12/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d;

import com.jkachele.game.physics2d.primitives.Circle;
import com.jkachele.game.physics2d.primitives.Ray2D;
import com.jkachele.game.physics2d.rigidbody.IntersectionDetector2D;
import com.jkachele.game.physics2d.primitives.Line2D;
import org.joml.Vector2f;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class CollisionDetectorTests {

    @Test
    public void pointOnLine2DEnd_TrueTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(0, 0);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLine2DEnd_TrueTest2() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(12, 4);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnVertLine2D_TrueTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(0, 10));
        Vector2f point = new Vector2f(0, 5);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOffLine2D_FalseTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(24, 8);
        assertFalse(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void raycastOnCircle_TrueTest() {
        Circle circle = new Circle();
        circle.setRadius(4);
        circle.setCenter(new Vector2f(6, 6));
        Ray2D ray = new Ray2D(new Vector2f(0, 0), new Vector2f(0.8f, 0.6f));
        assertTrue(IntersectionDetector2D.raycast(circle, ray, null));
    }

    @Test
    public void raycastOnCircle_FalseTest() {
        Circle circle = new Circle();
        circle.setRadius(4);
        circle.setCenter(new Vector2f(6, 6));
        Ray2D ray = new Ray2D(new Vector2f(0, 0), new Vector2f(10, 1));
        assertFalse(IntersectionDetector2D.raycast(circle, ray, null));
    }
}
