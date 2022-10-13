/******************************************
 *Project-------Learn-LWJGL
 *File----------Box2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.primitives;

import com.jkachele.game.physics2d.rigidbody.RigidBody2D;
import com.jkachele.game.util.GameMath;
import org.joml.Vector2f;

@SuppressWarnings("all")
public class Box2D {
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private RigidBody2D rigidBody = new RigidBody2D();

    public Box2D(Vector2f min, Vector2f max) {
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2.0f);
    }

    public Box2D() {
    }

    public Vector2f getMin() {
        return new Vector2f(this.rigidBody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getMax() {
        return new Vector2f(this.rigidBody.getPosition()).add(this.halfSize);
    }

    public Vector2f[] getVertices() {
        Vector2f min = getMin();
        Vector2f max = getMax();

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),
                new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y),
                new Vector2f(max.x, min.y),
        };

        if (GameMath.floatEquality(rigidBody.getRotationDeg(), 0)) {    // (rigidBody.getRotationDeg() != 0.0f)
            for (Vector2f vertex : vertices) {
                // TODO:IMPLEMENT THIS
                // Rotates point(Vector2f) around the center(Vector2f) by the rotation angle(float)
                GameMath.rotate(vertex, this.rigidBody.getPosition(), this.rigidBody.getRotationDeg());
            }
        }

        return vertices;
    }

    public RigidBody2D getRigidBody() {
        return rigidBody;
    }

    public Vector2f getHalfSize() {
        return this.halfSize;
    }
}
