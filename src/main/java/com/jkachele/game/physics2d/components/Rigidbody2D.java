/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Rigidbody2D.java
 *Author--------Justin Kachele
 *Date----------11/3/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2d.components;

import com.jkachele.game.components.Component;
import com.jkachele.game.physics2d.enums.BodyType;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

public class Rigidbody2D extends Component {
    private Vector2f velocity  = new Vector2f();
    private float angularDamping = 0.8f;    // Angular Friction
    private float linearDamping = 0.9f;     // Linear Friction
    private float mass = 0;
    private BodyType bodyType = BodyType.Dynamic;

    private boolean fixedRotation = false;
    private boolean continuousCollision = true;

    private transient Body rawBody = null;

    @Override
    public void update(float dt) {
        if (rawBody != null) {
            this.gameObject.transform.position.set(rawBody.getPosition().x, rawBody.getPosition().y);
            this.gameObject.transform.rotation = (float)Math.toDegrees(rawBody.getAngle());
        }
    }

    @Override
    public void destroy() {
        this.rawBody = null;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public boolean isContinuousCollision() {
        return continuousCollision;
    }

    public void setContinuousCollision(boolean continuousCollision) {
        this.continuousCollision = continuousCollision;
    }

    public Body getRawBody() {
        return rawBody;
    }

    public void setRawBody(Body rawBody) {
        this.rawBody = rawBody;
    }
}
