/******************************************
 *Project-------Learn-LWJGL
 *File----------Circle.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.primitives;

import com.jkachele.game.physics2d.rigidbody.RigidBody2D;
import org.joml.Vector2f;

public class Circle {
    private float radius = 1.0f;
    private RigidBody2D rigidBody2D = new RigidBody2D();

    public float getRadius() {
        return radius;
    }

    public Vector2f getCenter() {
        return rigidBody2D.getPosition();
    }
}