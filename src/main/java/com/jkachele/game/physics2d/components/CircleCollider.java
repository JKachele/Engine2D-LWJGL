/******************************************
 *Project-------Engine2D-LWJGL
 *File----------CircleCollider.java
 *Author--------Justin Kachele
 *Date----------11/3/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2d.components;

public class CircleCollider extends Collider {
    private float radius = 1;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
