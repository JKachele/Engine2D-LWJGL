/******************************************
 *Project-------Engine2D-LWJGL
 *File----------PhysicsSystem2D.java
 *Author--------Justin Kachele
 *Date----------10/18/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2d;

import com.jkachele.game.physics2d.forces.ForceRegistry;
import com.jkachele.game.physics2d.forces.Gravity2D;
import com.jkachele.game.physics2d.primitives.Collider2D;
import com.jkachele.game.physics2d.rigidbody.CollisionManifold;
import com.jkachele.game.physics2d.rigidbody.Collisions;
import com.jkachele.game.physics2d.rigidbody.Rigidbody2D;
import com.jkachele.game.util.GameMath;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem2D {
    private ForceRegistry forceRegistry;
    private Gravity2D gravity;

    private List<Rigidbody2D> rigidBodies;
    // Lists of colliding bodies with their manifolds
    private List<Rigidbody2D> rigidBodies1;
    private List<Rigidbody2D> rigidBodies2;
    private List<CollisionManifold> collisions;


    private float fixedUpdateTime;
    private float timeSinceLastUpdate = 0.0f;

    private final int impulseIterations = 6;

    public PhysicsSystem2D(float fixedUpdatedt, Vector2f gravity) {
        this.forceRegistry = new ForceRegistry();
        this.gravity = new Gravity2D(gravity);

        this.rigidBodies = new ArrayList<>();
        this.rigidBodies1 = new ArrayList<>();
        this.rigidBodies2 = new ArrayList<>();
        this.collisions = new ArrayList<>();

        this.fixedUpdateTime = fixedUpdatedt;
    }

    public void update(float dt) {
        timeSinceLastUpdate += dt;
        while (timeSinceLastUpdate >= fixedUpdateTime) {
            timeSinceLastUpdate -= fixedUpdateTime;
            fixedUpdate();
        }
    }

    public void fixedUpdate() {
        rigidBodies1.clear();
        rigidBodies2.clear();
        collisions.clear();

        // ====================================================================
        // Update the forces
        // ====================================================================
        forceRegistry.updateForces(fixedUpdateTime);

        // ====================================================================
        // Find any collisions
        // ====================================================================
        int size = rigidBodies.size();
        // TODO: optimize this loop. Currently looping through every pair of objects
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (i == j) {
                    continue;
                }

                CollisionManifold result = new CollisionManifold();
                Rigidbody2D r1 = rigidBodies.get(i);
                Rigidbody2D r2 = rigidBodies.get(j);
                Collider2D c1 = r1.getCollider();
                Collider2D c2 = r2.getCollider();

                if (c1 != null && c2 != null && !(r1.hasInfiniteMass()) && !(r2.hasInfiniteMass())) {
                    result = Collisions.findCollisionFeatures(c1, c2);
                }

                if (result != null && result.isColliding()) {
                    rigidBodies1.add(r1);
                    rigidBodies2.add(r2);
                    collisions.add(result);
                }
            }
        }

        // ====================================================================
        // Resolve collisions via iterative impulse resolution
        // ====================================================================
        for (int k = 0; k < impulseIterations; k++) {
            // Loop through all collisions
            for (int i = 0; i < collisions.size(); i++) {
                CollisionManifold collision = collisions.get(i);
                int jSize = collision.getContactPoints().size();
                // Loop through each collision point
                for (int j = 0; j < jSize; j++) {
                    Rigidbody2D r1 = rigidBodies1.get(i);
                    Rigidbody2D r2 = rigidBodies2.get(j);
                    applyImpulse(r1, r2, collision);
                }
            }
        }

        // ====================================================================
        // Update the velocities of all rigid bodies
        // ====================================================================
        for (Rigidbody2D rigidBody : rigidBodies) {
            rigidBody.physicsUpdate(fixedUpdateTime);
        }
    }

    private void applyImpulse(Rigidbody2D r1, Rigidbody2D r2, CollisionManifold collision) {
        // Linear velocity
        float invMass1 = r1.getInverseMass();
        float invMass2 = r2.getInverseMass();
        float invMassSum = invMass1 + invMass2;

        // 2 infinite mass objects
        if (GameMath.floatEquality(invMassSum, 0)) {
            return;
        }

        // Relative velocity
        Vector2f relativeVel = new Vector2f(r2.getLinearVelocity()).sub(r1.getLinearVelocity());
        Vector2f relativeNormal = new Vector2f(collision.getNormal()).normalize();

        // if moving away from each other, do nothing
        if (relativeVel.dot(relativeNormal) > 0) {
            return;
        }

        // Coefficient of restitution
        float e = Math.min(r1.getCOR(), r2.getCOR());
        float numerator = (-(1.0f + e) * relativeVel.dot(relativeNormal));
        float j = numerator / invMassSum;

        if (collision.getContactPoints().size() > 0 && !GameMath.floatEquality(j, 0)) {
            j /= (float)collision.getContactPoints().size();
        }

        Vector2f impulse = new Vector2f(relativeNormal).mul(j);
        r1.setLinearVelocity(new Vector2f(r1.getLinearVelocity())
                .sub(new Vector2f(impulse).mul(invMass1)));
        r2.setLinearVelocity(new Vector2f(r2.getLinearVelocity())
                .add(new Vector2f(impulse).mul(invMass2)));
    }

    public void addRigidBody(Rigidbody2D rigidBody, boolean addGravity) {
        rigidBodies.add(rigidBody);
        if (addGravity) {
            this.forceRegistry.add(rigidBody, gravity);
        }
    }
}
