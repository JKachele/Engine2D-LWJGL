/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Circle.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2dtmp.primitives;

import com.jkachele.game.physics2dtmp.rigidbody.Rigidbody2D;
import org.joml.Vector2f;

public class Circle extends Collider2D{
    private float radius = 1.0f;
    private Rigidbody2D rigidbody = new Rigidbody2D();

    public float getRadius() {
        return radius;
    }

    public float getRadiusSquared() {
        return radius * radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vector2f getCenter() {
        return rigidbody.getPosition();
    }

    public void setCenter(Vector2f center) {
        rigidbody.setTransform(center);
    }

    public Rigidbody2D getRigidbody() {
        return rigidbody;
    }

    public void setRigidbody(Rigidbody2D rigidbody) {
        this.rigidbody = rigidbody;
    }
}
