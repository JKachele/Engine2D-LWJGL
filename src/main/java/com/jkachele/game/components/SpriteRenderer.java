/******************************************
 *Project-------Engine2D-LWJGL
 *File----------SpriteRenderer.java
 *Author--------Justin Kachele
 *Date----------10/3/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.engine.Transform;
import com.jkachele.game.renderer.Texture;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;

    public SpriteRenderer() {
    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        // Set the Dirty flag if the transform of the game object was changed
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imgui() {
        float[] imColor = {color.x, color.y, color.z, color.w};
        ImGui.text("ColorPicker : ");
        if (ImGui.colorPicker4("", imColor)) {
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.isDirty = true;
        }
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getUvCoords() {
        return sprite.getUvCoords();
    }

    public void setSprite(Sprite sprite) {
        // Set the dirty flag if the sprite was changed
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor(Vector4f color) {
        // Set the dirty flag if the color was changed
        if (!this.color.equals(color)) {
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }

    public Vector4f getColor() {
        return color;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Transform getLastTransform() {
        return lastTransform;
    }

    public void setLastTransform(Transform lastTransform) {
        this.lastTransform = lastTransform;
    }

    public void setTexture(Texture texture) {
        this.sprite.setTexture(texture);
    }
}
