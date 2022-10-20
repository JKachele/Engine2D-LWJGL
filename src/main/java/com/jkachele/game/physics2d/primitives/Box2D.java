/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Box2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.primitives;

import com.jkachele.game.physics2d.rigidbody.Rigidbody2D;
import com.jkachele.game.util.GameMath;
import org.joml.Vector2f;

@SuppressWarnings("all")
public class Box2D {
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private Rigidbody2D rigidbody = new Rigidbody2D();

    public Box2D(Vector2f min, Vector2f max) {
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2.0f);
    }

    public Box2D() {
    }

    public Vector2f getLocalMin() {
        return new Vector2f(this.rigidbody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getLocalMax() {
        return new Vector2f(this.rigidbody.getPosition()).add(this.halfSize);
    }

    public Vector2f[] getVertices() {
        Vector2f min = getLocalMin();
        Vector2f max = getLocalMax();

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),
                new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y),
                new Vector2f(max.x, min.y),
        };

        if (GameMath.floatEquality(rigidbody.getRotationDeg(), 0)) {    // (rigidBody.getRotationDeg() != 0.0f)
            for (Vector2f vertex : vertices) {
                // Rotates point(Vector2f) around the center(Vector2f) by the rotation angle(float)
                GameMath.rotate(vertex, this.rigidbody.getPosition(), this.rigidbody.getRotationDeg());
            }
        }

        return vertices;
    }

    public Rigidbody2D getRigidbody() {
        return rigidbody;
    }

    public void setRigidbody(Rigidbody2D rigidbody) {
        this.rigidbody = rigidbody;
    }

    public void setSize(Vector2f size) {
        this.size = size;
        this.halfSize.set(size.div(2.0f));
    }

    public Vector2f getHalfSize() {
        return this.halfSize;
    }

    public Vector2f getCenter() {
        return this.rigidbody.getPosition();
    }
}
