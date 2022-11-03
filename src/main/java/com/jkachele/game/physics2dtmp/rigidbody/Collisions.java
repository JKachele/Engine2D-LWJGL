/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Collisions.java
 *Author--------Justin Kachele
 *Date----------10/20/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2dtmp.rigidbody;

import com.jkachele.game.physics2dtmp.primitives.Circle;
import com.jkachele.game.physics2dtmp.primitives.Collider2D;
import org.joml.Vector2f;

/**
 * Code to handle collisions
 */
public class Collisions {

    public static CollisionManifold findCollisionFeatures(Collider2D collider1, Collider2D collider2) {
        CollisionManifold result = null;

        if (collider1 instanceof Circle && collider2 instanceof Circle) {
            result = findCollisionFeatures((Circle)collider1, (Circle)collider2);
        } else {
            assert false : "Error: (Collisions): Unknown collider '" + collider1.getClass() + "' vs '" + collider2.getClass();
        }

        return result;
    }

    public static CollisionManifold findCollisionFeatures(Circle a, Circle b) {
        CollisionManifold result = new CollisionManifold();
        float radiusSum = a.getRadius() + b.getRadius();
        Vector2f distance = new Vector2f(b.getCenter()).sub(a.getCenter());
        if (distance.lengthSquared() - (radiusSum * radiusSum) > 0) {
            return result;
        }

        // Multiply by 0.5 to separate the circles by the same amount
        // TODO: Update to factor momentum and velocity
        float depth = Math.abs(distance.length() - radiusSum) * 0.5f;
        Vector2f normal = new Vector2f(distance).normalize();
        float distanceToPoint = a.getRadius() - depth;
        Vector2f contactPoint = new Vector2f(a.getCenter()).add(new Vector2f(normal).mul(distanceToPoint));

        result =  new CollisionManifold(normal, depth);
        result.addContactPoint(contactPoint);

        return result;
    }
}
