/******************************************
 *Project-------Learn-LWJGL
 *File----------RayCastResult.java
 *Author--------Justin Kachele
 *Date----------10/13/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.primitives;

import org.joml.Vector2f;

public class RayCastResult {
    private Vector2f point;
    private Vector2f normal;
    private float t;
    private boolean hit;

    public RayCastResult() {
        point = new Vector2f();
        normal = new Vector2f();
        t = -1;
        hit = false;
    }

    public void init(Vector2f point, Vector2f normal, float t, boolean hit) {
        this.point.set(point);
        this.normal.set(normal);
        this.t = t;
        this.hit = hit;
    }

    public static void reset(RayCastResult result) {
        if (result != null) {
            result.point.zero();
            result.normal.set(0, 0);
            result.t = -1;
            result.hit = false;
        }
    }
}
