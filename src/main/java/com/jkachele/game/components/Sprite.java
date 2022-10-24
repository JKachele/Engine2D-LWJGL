/******************************************
 *Project-------Engine2D-LWJGL
 *File----------Sprite.java
 *Author--------Justin Kachele
 *Date----------10/5/2022
 *License-------MIT License
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.renderer.Texture;
import org.joml.Vector2f;

public class Sprite {

    private Texture texture = null;
    private Vector2f[] uvCoords = defaultUV();
    private float width;
    private float height;

    public static Vector2f[] defaultUV() {
        Vector2f[] uvCoords = new Vector2f[4];
        uvCoords[0] = new Vector2f(1, 1);
        uvCoords[1] = new Vector2f(1, 0);
        uvCoords[2] = new Vector2f(0, 0);
        uvCoords[3] = new Vector2f(0, 1);
        return uvCoords;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2f[] getUvCoords() {
        return uvCoords;
    }

    public void setUvCoords(Vector2f[] uvCoords) {
        this.uvCoords = uvCoords;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTexID() {
        return texture == null? -1 : texture.getID();
    }
}
