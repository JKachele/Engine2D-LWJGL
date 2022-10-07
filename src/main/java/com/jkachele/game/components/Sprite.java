/******************************************
 *Project-------Learn-LWJGL
 *File----------Sprite.java
 *Author--------Justin Kachele
 *Date----------10/5/2022
 *License-------GNU GENERAL PUBLIC LICENSE
 ******************************************/
package com.jkachele.game.components;

import com.jkachele.game.renderer.Texture;
import lombok.Getter;
import org.joml.Vector2f;

@Getter
public class Sprite {

    private Texture texture;
    private Vector2f[] uvCoords;

    public Sprite(Texture texture) {
        this.texture = texture;
        this.uvCoords = defaultUV();
    }

    public Sprite(Texture texture, Vector2f[] uvCoords) {
        this.texture = texture;
        this.uvCoords = uvCoords;
    }

    public static Vector2f[] defaultUV() {
        Vector2f[] uvCoords = new Vector2f[4];
        uvCoords[0] = new Vector2f(1, 1);
        uvCoords[1] = new Vector2f(1, 0);
        uvCoords[2] = new Vector2f(0, 0);
        uvCoords[3] = new Vector2f(0, 1);
        return uvCoords;
    }
}
