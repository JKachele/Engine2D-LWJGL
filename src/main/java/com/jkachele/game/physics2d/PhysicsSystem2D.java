/******************************************
 *Project-------Engine2D-LWJGL
 *File----------PhysicsSystem2D.java
 *Author--------Justin Kachele
 *Date----------10/18/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.physics2d;

import com.jkachele.game.physics2d.forces.ForceRegistry;
import com.jkachele.game.physics2d.forces.Gravity2D;
import com.jkachele.game.physics2d.rigidbody.Rigidbody2D;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem2D {
    private ForceRegistry forceRegistry;
    private List<Rigidbody2D> rigidBodies;
    private Gravity2D gravity;
    private float fixedUpdateTime;
    private float timeSinceLastUpdate = 0.0f;

    public PhysicsSystem2D(float fixedUpdateDt, Vector2f gravity) {
        this.forceRegistry = new ForceRegistry();
        this.rigidBodies = new ArrayList<>();
        this.gravity = new Gravity2D(gravity);
        this.fixedUpdateTime = fixedUpdateDt;
    }

    public void update(float dt) {
        timeSinceLastUpdate += dt;
        while (timeSinceLastUpdate >= fixedUpdateTime) {
            timeSinceLastUpdate -= fixedUpdateTime;
            fixedUpdate();
        }
    }

    public void fixedUpdate() {
        forceRegistry.updateForces(fixedUpdateTime);

        // Update the velocities of all rigid bodies
        for (Rigidbody2D rigidBody : rigidBodies) {
            rigidBody.physicsUpdate(fixedUpdateTime);
        }
    }

    public void addRigidBody(Rigidbody2D rigidBody) {
        rigidBodies.add(rigidBody);
        this.forceRegistry.add(rigidBody, gravity);
    }
}
