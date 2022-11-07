/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Box2DCollider.java
 *Author--------Justin Kachele
 *Date----------11/3/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2d.components;

import com.jkachele.game.renderer.DebugDraw;
import org.joml.Vector2f;

public class Box2DCollider extends Collider {
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(super.offset);
        DebugDraw.addBox2D(center, this.halfSize, this.gameObject.transform.rotation);
    }

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    public Vector2f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2f origin) {
        this.origin = origin;
    }
}
