/******************************************
 *Project-------Learn-LWJGL
 *File----------Line2D.java
 *Author--------Justin Kachele
 *Date----------10/9/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.primitives;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Line2D {
    private Vector2f start;
    private Vector2f end;
    private Vector4f color;
    private int lifetime;

    public Line2D(Vector2f start, Vector2f end, Vector4f color, int lifetime) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifetime = lifetime;
    }

    public Line2D(Vector2f start, Vector2f end) {
        this.start = start;
        this.end = end;
    }

    public int beginFrame() {
        this.lifetime--;
        return lifetime;
    }

    public Vector2f getStart() {
        return start;
    }

    public Vector2f getEnd() {
        return end;
    }

    public Vector4f getColor() {
        return color;
    }

    public float lengthSquared() {
        return new Vector2f(end).sub(start).lengthSquared();
    }
}
