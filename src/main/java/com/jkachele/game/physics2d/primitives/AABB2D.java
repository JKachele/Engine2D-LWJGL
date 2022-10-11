/******************************************
 *Project-------Learn-LWJGL
 *File----------AABB2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.primitives;

import com.jkachele.game.physics2d.rigidbody.RigidBody2D;
import org.joml.Vector2f;

// Axis Aligned Bounding Box
public class AABB2D {
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private RigidBody2D rigidBody = new RigidBody2D();

    public AABB2D(Vector2f min, Vector2f max) {
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2.0f);
    }

    public AABB2D() {
    }

    public Vector2f getMin() {
        return new Vector2f(this.rigidBody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getMax() {
        return new Vector2f(this.rigidBody.getPosition()).add(this.halfSize);
    }
}
