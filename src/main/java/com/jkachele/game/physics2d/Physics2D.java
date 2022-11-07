/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Physics2D.java
 *Author--------Justin Kachele
 *Date----------11/3/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2d;

import com.jkachele.game.components.Transform;
import com.jkachele.game.engine.GameObject;
import com.jkachele.game.physics2d.components.Box2DCollider;
import com.jkachele.game.physics2d.components.CircleCollider;
import com.jkachele.game.physics2d.components.Rigidbody2D;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class Physics2D {
    private Vec2 gravity = new Vec2(0, -9.81f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;

    // Number of calculation passes to resolve collisions. Higher number = better physics but slows performance
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public void add(GameObject gameObject) {
        Rigidbody2D rigidBody = gameObject.getComponent(Rigidbody2D.class);
        if (rigidBody != null && rigidBody.getRawBody() == null) {
            Transform transform = gameObject.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rigidBody.getAngularDamping();
            bodyDef.linearDamping = rigidBody.getLinearDamping();
            bodyDef.fixedRotation = rigidBody.isFixedRotation();
            bodyDef.bullet = rigidBody.isContinuousCollision();

            switch (rigidBody.getBodyType()) {
                case Static -> bodyDef.type = BodyType.STATIC;
                case Dynamic -> bodyDef.type = BodyType.DYNAMIC;
                case Kinematic -> bodyDef.type = BodyType.KINEMATIC;
            }

            PolygonShape shape = new PolygonShape();
            CircleCollider circleCollider = gameObject.getComponent(CircleCollider.class);
            Box2DCollider boxCollider = gameObject.getComponent(Box2DCollider.class);

            if (circleCollider != null) {
                shape.setRadius(circleCollider.getRadius());
                bodyDef.position.set(transform.position.x, transform.position.y);
            } else if (boxCollider != null) {
                Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f);
                Vector2f offset = new Vector2f(boxCollider.getOffset());
                Vector2f origin = new Vector2f(boxCollider.getOrigin());
                shape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);

                Vec2 pos = bodyDef.position;
                float xPos = pos.x + offset.x;
                float yPos = pos.y + offset.y;
                bodyDef.position.set(xPos, yPos);
            }

            Body body = this.world.createBody(bodyDef);
            rigidBody.setRawBody(body);
            body.createFixture(shape, rigidBody.getMass());
        }
    }

    public void destroyGameObject(GameObject gameObject) {
        Rigidbody2D rigidBody = gameObject.getComponent(Rigidbody2D.class);
        if (rigidBody != null && rigidBody.getRawBody() != null) {
            world.destroyBody(rigidBody.getRawBody());
            rigidBody.destroy();
        }
    }

    public void update(float dt) {
        physicsTime += dt;
        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }

    }
}
