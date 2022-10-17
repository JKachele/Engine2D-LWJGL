/******************************************
 *Project-------Learn-LWJGL
 *File----------RigidBody2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.rigidbody;

import com.jkachele.game.components.Component;
import org.joml.Vector2f;

public class Rigidbody2D extends Component {
    private Vector2f position = new Vector2f();
    private float rotationDeg = 0.0f;

    private Vector2f linearVelocity = new Vector2f();
    private float angularVelocity = 0.0f;
    private float linearDamping = 0.0f;
    private float angularDamping = 0.0f;
    private boolean fixedRotation = false;

    public Vector2f getPosition() {
        return position;
    }

    public float getRotationDeg() {
        return rotationDeg;
    }

    public void setTransform(Vector2f position, float rotation) {
        this.position.set(position);
        this.rotationDeg = rotation;
    }

    public void setTransform(Vector2f position) {
        this.position.set(position);
    }
}
