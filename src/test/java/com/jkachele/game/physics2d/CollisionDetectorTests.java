/******************************************
 *Project-------Learn-LWJGL
 *File----------CollisionDetectorTests.java
 *Author--------Justin Kachele
 *Date----------10/12/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d;

import com.jkachele.game.physics2d.rigidbody.IntersectionDetector2D;
import com.jkachele.game.renderer.Line2D;
import org.joml.Vector2f;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;


public class CollisionDetectorTests {

    @Test
    public void pointOnLine2DEndShouldReturnTrueTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(0, 0);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLine2DEndShouldReturnTrueTest2() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(12, 4);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnVertLine2DShouldReturnTrueTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(0, 10));
        Vector2f point = new Vector2f(0, 5);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

}
