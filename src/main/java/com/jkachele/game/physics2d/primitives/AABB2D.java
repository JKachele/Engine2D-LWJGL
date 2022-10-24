/******************************************
 *Project-------Engine2D-LWJGL
 *File----------AABB2D.java
 *Author--------Justin Kachele
 *Date----------10/11/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.physics2d.primitives;

import com.jkachele.game.physics2d.rigidbody.Rigidbody2D;
import org.joml.Vector2f;

// Axis Aligned Bounding Box
public class AABB2D {
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private Rigidbody2D rigidbody = new Rigidbody2D();

    public AABB2D(Vector2f min, Vector2f max) {
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2.0f);
    }

    public AABB2D() {
    }

    public Vector2f getMin() {
        return new Vector2f(this.rigidbody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getMax() {
        return new Vector2f(this.rigidbody.getPosition()).add(this.halfSize);
    }

    public Rigidbody2D getRigidbody() {
        return rigidbody;
    }

    public void setRigidbody(Rigidbody2D rigidbody) {
        this.rigidbody = rigidbody;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
        this.halfSize.set(size.div(2.0f));
    }
}
