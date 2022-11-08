/******************************************
 *Project-------Engine2D-LWJGL
 *File----------RigidBody2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------Mozilla Public License Version 2.0
 ******************************************/
package com.jkachele.game.physics2dtmp.rigidbody;

import com.jkachele.game.components.Component;
import com.jkachele.game.components.Transform;
import com.jkachele.game.physics2dtmp.primitives.Collider2D;
import com.jkachele.game.util.GameMath;
import org.joml.Vector2f;

public class Rigidbody2D extends Component {
    private Transform rawTransform;
    private Collider2D collider;

    private Vector2f position = new Vector2f();
    private float rotationDeg = 0.0f;
    private float mass = 0.0f;
    private float inverseMass = 0.0f;
    private Vector2f forceAccumulator = new Vector2f();

    private Vector2f linearVelocity = new Vector2f();
    private float angularVelocity = 0.0f;
    private float linearDamping = 0.0f;
    private float angularDamping = 0.0f;
    private float COR = 1.0f;       // Coefficient of restitution
    private boolean fixedRotation = false;

    public void physicsUpdate(float dt) {
        if (this.mass == 0.0f) {
            return;
        }

        // Calculate the linear velocity
        Vector2f acceleration = new Vector2f(forceAccumulator);
        acceleration.mul(inverseMass);
        linearVelocity.add(acceleration.mul(dt));

        // Calculate the linear position
        this.position.add(new Vector2f(linearVelocity).mul(dt));

        syncCollisionTransforms();
        clearAccumulators();
    }

    public void syncCollisionTransforms() {
        if (rawTransform != null) {
            rawTransform.position.set(position);
        }
    }

    public void clearAccumulators() {
        this.forceAccumulator.zero();
    }

    public void addForce(Vector2f force) {
        forceAccumulator.add(force);
    }

    public  void addVelocity(Vector2f velocity) {
        linearVelocity.add(velocity);
    }

    public boolean hasInfiniteMass() {
        return GameMath.floatEquality(this.mass, 0);
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getRotationDeg() {
        return rotationDeg;
    }

    public float getCOR() {
        return COR;
    }

    public void setCOR(float COR) {
        this.COR = COR;
    }

    public void setTransform(Vector2f position, float rotation) {
        this.position.set(position);
        this.rotationDeg = rotation;
    }

    public void setTransform(Vector2f position) {
        this.position.set(position);
    }

    public Transform getRawTransform() {
        return rawTransform;
    }

    public void setRawTransform(Transform rawTransform) {
        this.rawTransform = rawTransform;
        this.position.set(rawTransform.position);
    }

    public Vector2f getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(Vector2f linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        if (mass != 0.0f) {
            this.inverseMass = 1.0f / mass;
        }
    }

    public float getInverseMass() {
        return inverseMass;
    }

    public void setCollider(Collider2D collider) {
        this.collider = collider;
    }

    public Collider2D getCollider() {
        return this.collider;
    }
}
