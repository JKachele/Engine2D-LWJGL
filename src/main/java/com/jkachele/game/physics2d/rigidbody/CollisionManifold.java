/******************************************
 *Project-------Engine2D-LWJGL
 *File----------CollisionManifold.java
 *Author--------Justin Kachele
 *Date----------10/20/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.rigidbody;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CollisionManifold {
    private boolean isColliding;
    private Vector2f normal;
    private List<Vector2f> contactPoints;
    private float collisionDepth;

    public CollisionManifold(Vector2f normal, float collisionDepth) {
        this.isColliding = false;
        this.normal = normal;
        this.collisionDepth = collisionDepth;
        contactPoints = new ArrayList<>();
    }

    public CollisionManifold() {
        isColliding = false;
        normal = new Vector2f(0, 0);
        contactPoints = new ArrayList<>();
        collisionDepth = 0;
    }

    public void addContactPoint(Vector2f contactPoint) {
        contactPoints.add(contactPoint);
    }

    public boolean isColliding() {
        return isColliding;
    }

    public void setColliding(boolean colliding) {
        isColliding = colliding;
    }

    public Vector2f getNormal() {
        return normal;
    }

    public void setNormal(Vector2f normal) {
        this.normal = normal;
    }

    public List<Vector2f> getContactPoints() {
        return contactPoints;
    }

    public float getCollisionDepth() {
        return collisionDepth;
    }

    public void setCollisionDepth(float collisionDepth) {
        this.collisionDepth = collisionDepth;
    }
}
