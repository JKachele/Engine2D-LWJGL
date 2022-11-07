/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Transform.java
 *Author--------Justin Kachele
 *Date----------10/4/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.editor.GameImGui;
import com.jkachele.game.util.GameMath;
import org.joml.Vector2f;

public class Transform extends Component {
    public Vector2f position;
    public Vector2f scale;
    public float rotation = 0;
    public int zIndex;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
        this.zIndex = 0;
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public void imGui() {
        GameImGui.drawVec2Control("Position", this.position);
        GameImGui.drawVec2Control("Scale", this.scale, 32.0f);
        this.rotation = GameImGui.dragFloat("Rotation", this.rotation);
        this.zIndex = GameImGui.dragInt("Z Index", this.zIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Transform t)) return false;

        return t.position.equals(this.position) && t.scale.equals(this.scale) &&
                GameMath.floatEquality(t.rotation, this.rotation);
    }
}
