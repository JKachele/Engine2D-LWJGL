/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Collisions.java
 *Author--------Justin Kachele
 *Date----------10/20/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d.rigidbody;

import com.jkachele.game.physics2d.primitives.Circle;
import org.joml.Vector2f;

/**
 * Code to handle collisions
 */
public class Collisions {

    public static CollisionManifold findCollisionFeatures(Circle a, Circle b) {
        CollisionManifold result = new CollisionManifold();
        float radiusSum = a.getRadius() + b.getRadius();
        Vector2f distance = new Vector2f(b.getCenter().sub(a.getCenter()));
        if (distance.lengthSquared() - radiusSum * radiusSum > 0) {
            return result;
        }

        // Multiply by 0.5 to separate the circles by the same amount
        // TODO: Update to factor momentum and velocity
        float depth = Math.abs((distance.length() - radiusSum) * 0.5f);
        Vector2f normal = new Vector2f(distance).normalize();
        float distanceToPoint = a.getRadius() - depth;
        Vector2f contactPoint = new Vector2f(a.getCenter().add(new Vector2f(normal).mul(distanceToPoint)));

        result =  new CollisionManifold(normal, depth);
        result.addContactPoint(contactPoint);

        return result;
    }
}
